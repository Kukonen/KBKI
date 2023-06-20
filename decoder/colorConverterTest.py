from PIL import Image, ImageDraw


# Heght of image from file properties
imageHeight = 0
# Width of image from file properties
imageWidth = 0
filterType = 0
colorType = 0
compressionType = 0
rows = 0

palette = []

def readKBKI(filename):
    with open(filename) as f:
        global imageWidth
        global imageHeight

        imageHeight = int(f.read(32), 2)
        imageWidth = int(f.read(32), 2)
        filterType = int(f.read(8), 2)
        colorType = int(f.read(8), 2)
        compressionType = int(f.read(8), 2)

        pixelFilter = 0
        for y in range(imageHeight):
            for x in range(imageWidth + 1):
                if x % (imageWidth + 1) == 0:
                    pixelFilter = int(f.read(8), 2)
                    continue

                color = '#'

                for j in range(6):
                    color += str(hex(int(f.read(4), 2)))[2:]

                palette.append(color)
                print(color)
        

def generateImage(filename):
    readKBKI(filename)

    global imageWidth
    global imageHeight
    
    # Number of pixels
    numberOfPixels = imageWidth * imageHeight
        
    pixelSize = 100

    # Creating new Image based on file
    result = Image.new(mode="RGB", size=(imageWidth * pixelSize, imageHeight * pixelSize))

    x = 0
    y = 0

    for i in range(numberOfPixels):
        pixel = Image.new(mode="RGB", size=(pixelSize, pixelSize), color=palette[i])
        result.paste(pixel, (x, y))
        if (i + 1) % imageWidth == 0:
            x = 0
            y += pixelSize
        else:
            x += pixelSize

    result.show()

#readKBKI("KBKImage.txt")
generateImage("KBKImage.txt")
