#include <opencv2/imgcodecs.hpp>
#include <opencv2/highgui.hpp>
#include <opencv2/imgproc.hpp>
#include <iostream>
#include <fstream>
#include <algorithm>

using namespace cv;
using namespace std;

class WriteStrategy 
{
public:
	virtual void write(Mat& img, ofstream& fout, int filter, int rows, int cols) = 0;
};

class WriteStrategy3Channels : public WriteStrategy
{
public:
	virtual void write(Mat& img, ofstream& fout, int filter, int rows, int cols) override
	{
		for (int i = 0; i < rows; ++i)
		{
			fout.write(reinterpret_cast<const char*>(&filter), sizeof(char));
			for (int j = 0; j < cols; ++j)
			{
				Vec3b Pixel = img.at<Vec3b>(i, j);

				fout.write(reinterpret_cast<const char*>(&Pixel[2]), sizeof(char));
				fout.write(reinterpret_cast<const char*>(&Pixel[1]), sizeof(char));
				fout.write(reinterpret_cast<const char*>(&Pixel[0]), sizeof(char));
			}
		}
	}
};

class WriteStrategy4Channels : public WriteStrategy
{
public:
	virtual void write(Mat& img, ofstream& fout, int filter, int rows, int cols) override
	{
		for (int i = 0; i < rows; ++i)
		{
			fout.write(reinterpret_cast<const char*>(&filter), sizeof(char));
			for (int j = 0; j < cols; ++j)
			{
				Vec4b Pixel = img.at<Vec4b>(i, j);

				fout.write(reinterpret_cast<const char*>(&Pixel[0]), sizeof(char));
				fout.write(reinterpret_cast<const char*>(&Pixel[1]), sizeof(char));
				fout.write(reinterpret_cast<const char*>(&Pixel[2]), sizeof(char));
				fout.write(reinterpret_cast<const char*>(&Pixel[3]), sizeof(char));
			}
		}
	}
};

class Writer
{
protected:
	WriteStrategy* writeStrategy = nullptr;

public:
	Writer(WriteStrategy* writeStrategy)
	{
		this->writeStrategy = writeStrategy;
	}

	void write(Mat& img, ofstream& fout, int filter, int rows, int cols)
	{
		writeStrategy->write(img, fout, filter, rows, cols);
	}
};

void makingMetaData(int& version, int& filter, int& colorType, int& compressionType, int& encryptionType, int& key, char **argv, const int argc)
{
	if(argc > 2)
		for (int i = 0; argv[2][i]; i++)
			version = version * 10 + (int(argv[2][i]) - 48);

	if (argc > 3)
		for (int i = 0; argv[3][i]; i++)
			filter = filter * 10 + (int(argv[3][i]) - 48);

	if (argc > 4)
		for (int i = 0; argv[4][i]; i++)
			colorType = colorType * 10 + (int(argv[4][i]) - 48);

	if (argc > 5)
		for (int i = 0; argv[5][i]; i++)
			compressionType = compressionType * 10 + (int(argv[5][i]) - 48);

	if (argc > 6)
		for (int i = 0; argv[6][i]; i++)
			encryptionType = encryptionType * 10 + (int(argv[6][i]) - 48);

	if (argc > 7)
		for (int i = 0; argv[7][i]; i++)
			key = key * 10 + (int(argv[7][i]) - 48);
}
 
int main(int argc, char* argv[])
{
	int version = 0, filter = 0, colorType = 0, compressionType = 0, encryptionType = 0, key = 0, rows = 0, cols = 0;
	bool alphaChannel = false;
	string file_path("imagePixels.kbki");
	string path = argv[1];

	Mat img = imread(path), tempImage;
	ofstream fout;

	rows = img.rows;
	cols = img.cols;

	fout.open(file_path);

	makingMetaData(version, filter, colorType, compressionType, encryptionType, key, argv, argc);

	alphaChannel = bool(1 & colorType);
	colorType >>= 1;
	
	if (!alphaChannel)
	{
		if (colorType == 0)
			cvtColor(img, tempImage, COLOR_BGR2RGB);
		else if (colorType == 4)
			cvtColor(img, tempImage, COLOR_RGB2Lab);
		else if (colorType == 5)
			cvtColor(img, tempImage, COLOR_RGB2HSV);
		else if (colorType == 6)
			cvtColor(img, tempImage, COLOR_RGB2HLS);

	}
	else 
	{
		if (colorType == 0)
			cvtColor(img, tempImage, COLOR_BGR2BGRA);
	}

	fout.write(reinterpret_cast<const char*>(&version), sizeof(char));  //Version
	reverse(reinterpret_cast<char*>(&img.rows), reinterpret_cast<char*>(&img.rows) + sizeof(int));
	fout.write(reinterpret_cast<const char*>(&img.rows), sizeof(int));  //Height of image
	reverse(reinterpret_cast<char*>(&img.cols), reinterpret_cast<char*>(&img.cols) + sizeof(int));
	fout.write(reinterpret_cast<const char*>(&img.cols), sizeof(int));  //Weight of image
	fout.write(reinterpret_cast<const char*>(&filter), sizeof(char));  //Filter
	fout.write(reinterpret_cast<const char*>(&colorType), sizeof(char)); //Color type of image
	fout.write(reinterpret_cast<const char*>(&compressionType), sizeof(char));  //Compression type of image
	fout.write(reinterpret_cast<const char*>(&encryptionType), sizeof(char));  //Encryption type of image

	if(encryptionType != 0)
		fout.write(reinterpret_cast<const char*>(&key), sizeof(char));  //Key of encryption

	if (!alphaChannel)
	{
		Writer* writer = new Writer(new WriteStrategy3Channels);
		writer->write(tempImage, fout, filter, rows, cols);
	}
	else 
	{
		Writer* writer = new Writer(new WriteStrategy4Channels);
		writer->write(tempImage, fout, filter, rows, cols);
	}

	fout.close();
	return 0;
}