#!/usr/bin/env bash
set -euo pipefail

font_dir="./*.ttf"
font_dir_otf="./*.otf"

for file in $font_dir; do
    mkdir -p ~/.local/share/fonts/custom
    cp "$file" ~/.local/share/fonts/custom
    fc-cache -vf ~/.local/share/fonts/custom

for file in $font_dir_otf; do
    mkdir -p ~/.local/share/fonts/custom
    cp "$file" ~/.local/share/fonts/custom
    fc-cache -vf ~/.local/share/fonts/custom
