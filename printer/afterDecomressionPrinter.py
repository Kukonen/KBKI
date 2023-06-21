from sys import argv
from KBKIDecoder import KBKIDecoder

# print file after decompress by parametrs
# typcly use in java decompessor after decompress

scriptname, height, width, palette = argv
decoder = KBKIDecoder('decompossive', 
                      arg1=height, 
                      arg2=width, 
                      arg3=palette.split(',')
                    )
decoder.generateImage()