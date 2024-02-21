#!/usr/bin/env bash
set -eo pipefail

echo "Checking CPU..."

if [[ "$(uname)" != "Linux" ]]; then
    echo "Script must be run on a Linux system."
    exit 1
fi

min_cpus=2
min_l2_cache=524288 # minimum L2 cache size in bytes
min_l3_cache=4194304 # minimum L3 cache size in bytes
min_clock_speed=1000 # minimum clock speed in MHz
required_flags=(sse sse2 ssse3 popcnt smep sgx fma3 avx avx2 avx512f rdrand)
tpms=$(ls /dev/tpm*)

cpuinfo=$(cat /proc/cpuinfo | grep flags | head -n 1)
num_cpus=$(nproc --all)
l2_cache=$(awk '/^L2/{print $2}' /proc/cpuinfo | paste -sd+ | bc)
l3_cache=$(awk '/^L3/{print $2}' /proc/cpuinfo | paste -sd+ | bc)
clock_speed=$(cat /sys/devices/system/cpu/cpu*/cpufreq/cpuinfo_max_freq | sort -nr | awk 'NR==1{print $1/1000}')

missing_flags=false
for flag in "${required_flags[@]}"; do
    if [[ ! "$cpuinfo" =~ "$flag" ]]; then
        missing_flags=true
        break
    fi
done

if (( num_cpus < min_cpus )) || (( l2_cache < min_l2_cache )) || (( l3_cache < min_l3_cache )) || (( clock_speed < min_clock_speed )) || $missing_flags || [[ -z "$tpms" ]]; then
    message="CPU does not meet the minimum requirements!"
    
    if [[ -z "$tpms" ]]; then
        message="$message\nTPM module not found!"
    fi
    
    if $missing_flags; then
        message="$message\nMissing one or more required flags:"
        for flag in "${required_flags[@]}"; do
            message="$message\n$flag"
        done
    fi

    echo -e "\033[1;31m${message}\033[0m"
else
    echo "CPU meets the minimum requirements!"
fi
