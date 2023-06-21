from sys import argv
from KBKIDecoder import KBKIDecoder

scriptname, filename = argv
decoder = KBKIDecoder(filename)
decoder.generateImage()