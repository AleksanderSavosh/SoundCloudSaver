#!/bin/bash
#name    dpi    scale size
#ldpi    120dpi 3     32
#mdpi    160dpi 4     43
#hdpi    240dpi 6     64
#xhdpi   320dpi 8     86
#xxhdpi  480dpi 12    128
#xxxhdpi 640dpi 16    170

#size for hdpi, number must div on 6
files="svg/*.svg"
basesize=54
let koefsize=$basesize/6

let ldpisize=$koefsize*3
ldpisize="${ldpisize}x${ldpisize}"
echo "ldpi size $ldpisize"

let mdpisize=$koefsize*4
mdpisize="${mdpisize}x${mdpisize}"
echo "mdpi size $mdpisize"

let hdpisize=$koefsize*6
hdpisize="${hdpisize}x${hdpisize}"
echo "hdpi size $hdpisize"

let xhdpisize=$koefsize*8
xhdpisize="${xhdpisize}x${xhdpisize}"
echo "xhdpi size $xhdpisize"

let xxhdpisize=$koefsize*12
xxhdpisize="${xxhdpisize}x${xxhdpisize}"
echo "xxhdpi size $xxhdpisize"

let xxxhdpisize=$koefsize*16
xxxhdpisize="${xxxhdpisize}x${xxxhdpisize}"
echo "xxxhdpi size $xxxhdpisize"

#rm -rf ../res/drawable ../res/drawable-ldpi ../res/drawable-mdpi ../res/drawable-hdpi ../res/drawable-xhdpi ../res/drawable-xxhdpi ../res/drawable-xxxhdpi
mkdir -p ../res/drawable ../res/drawable-ldpi ../res/drawable-mdpi ../res/drawable-hdpi ../res/drawable-xhdpi ../res/drawable-xxhdpi ../res/drawable-xxxhdpi

for f in svg/*.svg; do
    filename=`basename $f .svg`.png
    echo "Converting $f to $filename"
    convert -density 240 -background transparent $f -resize ${hdpisize} -gravity center -extent ${hdpisize} ../res/drawable/"$filename"

    convert -density 120 -background transparent $f -resize ${ldpisize} -gravity center -extent ${ldpisize} ../res/drawable-ldpi/"$filename"
    convert -density 160 -background transparent $f -resize ${mdpisize} -gravity center -extent ${mdpisize} ../res/drawable-mdpi/"$filename"
    convert -density 240 -background transparent $f -resize ${hdpisize} -gravity center -extent ${hdpisize} ../res/drawable-hdpi/"$filename"
    convert -density 320 -background transparent $f -resize ${xhdpisize} -gravity center -extent ${xhdpisize} ../res/drawable-xhdpi/"$filename"
    convert -density 480 -background transparent $f -resize ${xxhdpisize} -gravity center -extent ${xxhdpisize} ../res/drawable-xxhdpi/"$filename"
    convert -density 640 -background transparent $f -resize ${xxxhdpisize} -gravity center -extent ${xxxhdpisize} ../res/drawable-xxxhdpi/"$filename"
done;

echo 'Done'

