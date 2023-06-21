from sys import argv
from KBKIDecoder import KBKIDecoder

# print non-compressed file by filename

scriptname, filename = argv
decoder = KBKIDecoder('filename', filename)
decoder.generateImage()