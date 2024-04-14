#include <opencv2/opencv.hpp> // Include OpenCV library for loading images
#include <string> // For string handling
using namespace std;
using namespace cv;

class Overlay {
private:
    Mat loginImage; // Holds the loaded image data
public:
    Overlay() {
        // Load the image from file
        loginImage = imread("login.png", IMREAD_COLOR);
        if (!loginImage.data) {
            cout << "Error opening 'login.png', shutting down application." << endl;
            exit(500);
        }
    }

    void displayImage(int width = 800, int height = 600) {
        // Create a window with specified dimensions
        namedWindow("Login Window", WINDOW_NORMAL);
        resizeWindow("Login Window", width, height);

        // Display the loaded image in the created window
        imshow("Login Window", loginImage);

        // Wait for user input before closing the window
        waitKey();
    }
};

int main() {
    Overlay overlay;
    overlay.displayImage();
    return 0;
}
