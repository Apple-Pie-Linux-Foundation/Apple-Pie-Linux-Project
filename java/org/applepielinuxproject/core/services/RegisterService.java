import java.io.File;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

interface ServiceImplementation {
    void init() throws Exception;
    void start() throws Exception;
    void stop() throws Exception;
}

enum RegisterStatus {
    REGISTERED, DELETED, NOT_FOUND
}

class RegisterService {
    private static final Map<Long, RegistrationData> SERVICES = new HashMap<>();

    public static void registerService(long identifier, String name, File... codes) throws Exception {
        if (SERVICES.containsKey(identifier)) {
            throw new IllegalArgumentException("A service already exists with the specified identifier.");
        }

        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        Class<?>[] classes = new Class[codes.length];
        for (int i = 0; i < codes.length; ++i) {
            classes[i] = classloader.loadClass(codes[i].getAbsolutePath());
        }

        ServiceImplementation serviceImpl = (ServiceImplementation) Proxy.newProxyInstance(
                classloader,
                new Class[]{ServiceImplementation.class},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        switch (method.getName()) {
                            case "init":
                                initService((RegistrationData) args[0]);
                                break;
                            case "start":
                                startService((RegistrationData) args[0]);
                                break;
                            case "stop":
                                stopService((RegistrationData) args[0]);
                                break;
                            default:
                                throw new UnsupportedOperationException("Unrecognized method name: " + method.getName());
                        }
                        return null;
                    }

                    private void initService(RegistrationData registrationData) throws Exception {
                        registrationData.status = RegisterStatus.REGISTERED;
                        registrationData.implementation = (ServiceImplementation) Proxy.newProxyInstance(
                                classloader,
                                classes,
                                new InvocationHandler() {
                                    @Override
                                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                                        switch (method.getName()) {
                                            case "init":
                                                registrationData.instance.init();
                                                break;
                                            case "start":
                                                registrationData.instance.start();
                                                break;
                                            case "stop":
                                                registrationData.instance.stop();
                                                break;
                                            default:
                                                throw new UnsupportedOperationException("Unrecognized method name: " + method.getName());
                                        }
                                        return null;
                                    }

                                    private final RegistrationData registrationData;

                                    public RegistrationData getRegistrationData() {
                                        return registrationData;
                                    }

                                    public RegistrationData(Class<?>[] classes, long identifier, String name) throws Exception {
                                        this.registrationData = new RegistrationData();
                                        this.registrationData.classes = classes;
                                        this.registrationData.identifier = identifier;
                                        this.registrationData.name = name;
                                        this.registrationData.status = RegisterStatus.NOT_FOUND;
                                        this.registrationData.instance = null;
                                        SERVICES.put(identifier, this.registrationData);
                                        this.registrationData.instance = (ServiceImplementation) Proxy.newProxyInstance(
                                                classloader,
                                                classes,
                                                this);
                                    }
                                });
                    }

                    private void startService(RegistrationData registrationData) throws Exception {
                        if (registrationData.status != RegisterStatus.DELETED && registrationData.status != RegisterStatus.NOT_FOUND) {
                            registrationData.instance.start();
                        }
                    }

                    private void stopService(RegistrationData registrationData) throws Exception {
                        if (registrationData.status != RegisterStatus.DELETED && registrationData.status != RegisterStatus.NOT_FOUND) {
                            registrationData.instance.stop();
                        }
                    }
                });
            }

            SERVICES.computeIfAbsent(identifier, id -> new RegistrationData()).getRegistrationData().init(classes, identifier, name);

            if (!args[3].equalsIgnoreCase("create_service")) {
                executeCommand(identifier, name, args[4]);
            }
        }

        private static void executeCommand(long identifier, String name, String command) {
            var registrationData = SERVICES.getOrDefault(identifier, new RegistrationData());
            if (registrationData.status == RegisterStatus.REGISTERED || registrationData.status == RegisterStatus.RESTORED) {
                switch (command) {
                    case "start":
                        registrationData.instance.start();
                        break;
                    case "stop":
                        registrationData.instance.stop();
                        break;
                    case "delete":
                        registrationData.status = RegisterStatus.DELETED;
                        break;
                    case "restore_deleted_service":
                        registrationData.status = RegisterStatus.REGISTERED;
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown command: " + command);
                }
            } else {
                throw new IllegalArgumentException("Service with identifier '0x" + Long.toHexString(identifier) + "' does not exist or has been deleted.");
            }
        }
    }

    private static class RegistrationData {
        long identifier;
        String name;
        RegisterStatus status;
        ServiceImplementation instance;
        Class<?>[] classes;
    }
}
