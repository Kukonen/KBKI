from PIL import Image
from KBKI import KBKI

# Class works with plug on filter byte

class KBKIDecoder:
    __KBKI = KBKI()

    def __init__(self, filename):
        self.filename = filename
        self.__readKBKI(filename)

    #read uncompression file and write pixels code in colnsole
    # so ignore filter byte! 
    def __readKBKI(self, filename):

        with open(filename) as f:
            self.__KBKI.__imageHeight = int(f.read(32), 2)
            self.__KBKI.__imageWidth = int(f.read(32), 2)
            self.__KBKI.__filterType = int(f.read(8), 2)
            self.__KBKI.__colorType = int(f.read(8), 2)
            self.__KBKI.__compressionType = int(f.read(8), 2)

            pixelFilter = 0
            for y in range(self.__KBKI.__imageHeight):
                for x in range(self.__KBKI.__imageWidth + 1):
                    if x % (self.__KBKI.__imageWidth + 1) == 0:
                        pixelFilter = int(f.read(8), 2)
                        continue

                    color = '#'

                    for j in range(6):
                        color += str(hex(int(f.read(4), 2)))[2:]
                    self.__KBKI.palette.append(color)
                    print(color)

    # generate image from palitte pixels
    def generateImage(self, pixelSize = 100):
        
        # Number of pixels
        numberOfPixels = self.__KBKI.__imageWidth * self.__KBKI.__imageHeight

        # Creating new Image based on file
        result = Image.new(mode="RGB", size=(self.__KBKI.__imageWidth * pixelSize, self.__KBKI.__imageHeight * pixelSize))

        x = 0
        y = 0

        for i in range(numberOfPixels):
            pixel = Image.new(
                mode="RGB", 
                size=(pixelSize, pixelSize), 
                color=self.__KBKI.palette[i]
                )
            result.paste(pixel, (x, y))
            if (i + 1) % self.__KBKI.__imageWidth == 0:
                x = 0
                y += pixelSize
            else:
                x += pixelSize

        result.show()