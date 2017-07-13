#include "com_example_anannyauberoi_appfinal_cvClass.h"
#include <string>
#include <sstream>
using namespace std;
using namespace cv;

JNIEXPORT jstring JNICALL Java_com_example_anannyauberoi_appfinal_cvClass_detect
  (JNIEnv *env, jclass obj, jlong addrRgba){
        Mat& frame=*(Mat*)addrRgba;
        double x= modifiedLaplacian(frame);
        //Convert double to String so that it can be returned to the Java environment.
        std::ostringstream convert;
        convert << x;
        std::string Result=convert.str();
        const char *finalResult = Result.c_str();

        return env->NewStringUTF(finalResult);        //finalResult is the char* version of double.
  }
  Mat I;

 //modified laplacian is just the name.The code has been replaced by a better optimised version

  double modifiedLaplacian(Mat& src)
  {
  /*
      This function returns a double number which denotes the sharpness of current frame.
      The higher the number, the sharper the image.
      Other focus measure functions can be tried here.

  Mat src_gray;                                   //Declare a matrix to store grayscale image.
      cvtColor( src, src_gray, CV_BGR2GRAY );     //Convert image to grayscale.
      Mat M = (Mat_<double>(3, 1) << -1, 2, -1);  //Now apply the focus measure function on it.
      Mat G = getGaussianKernel(3, -1, CV_64F);
      Mat Lx;
      sepFilter2D(src_gray, Lx, CV_64F, M, G);
      Mat Ly;
      sepFilter2D(src_gray, Ly, CV_64F, G, M);
      Mat FM = abs(Lx) + abs(Ly);
      double focusMeasure = mean(FM).val[0];
      return focusMeasure;
  */
  //Mat I = imread("15.png", CV_LOAD_IMAGE_GRAYSCALE);
    I=src;
      if( I.empty())
          return -1;
      //Size sz(480,320);
      //resize(I,I,sz);
      Mat padded;                            //expand input image to optimal size
      int m = getOptimalDFTSize( I.rows );
      int n = getOptimalDFTSize( I.cols ); // on the border add zero values
      copyMakeBorder(I, padded, 0, m - I.rows, 0, n - I.cols, BORDER_CONSTANT, Scalar::all(0));

      Mat planes[] = {Mat_<float>(padded), Mat::zeros(padded.size(), CV_32F)};
      Mat complexI;
      merge(planes, 2, complexI);         // Add to the expanded another plane with zeros

      dft(complexI, complexI);            // this way the result may fit in the source matrix

      // compute the magnitude and switch to logarithmic scale
      // => log(1 + sqrt(Re(DFT(I))^2 + Im(DFT(I))^2))
      split(complexI, planes);                   // planes[0] = Re(DFT(I), planes[1] = Im(DFT(I))
      magnitude(planes[0], planes[1], planes[0]);// planes[0] = magnitude
      Mat magI = planes[0];

      magI += Scalar::all(1);                    // switch to logarithmic scale
      log(magI, magI);

      // crop the spectrum, if it has an odd number of rows or columns
      magI = magI(Rect(0, 0, magI.cols & -2, magI.rows & -2));

      // rearrange the quadrants of Fourier image  so that the origin is at the image center
      int cx = magI.cols/2;
      int cy = magI.rows/2;

      Mat q0(magI, Rect(0, 0, cx, cy));   // Top-Left - Create a ROI per quadrant
      Mat q1(magI, Rect(cx, 0, cx, cy));  // Top-Right
      Mat q2(magI, Rect(0, cy, cx, cy));  // Bottom-Left
      Mat q3(magI, Rect(cx, cy, cx, cy)); // Bottom-Right

      Mat tmp;                           // swap quadrants (Top-Left with Bottom-Right)
      q0.copyTo(tmp);
      q3.copyTo(q0);
      tmp.copyTo(q3);

      q1.copyTo(tmp);                    // swap quadrant (Top-Right with Bottom-Left)
      q2.copyTo(q1);
      tmp.copyTo(q2);

      normalize(magI, magI, 0, 1, CV_MINMAX); // Transform the matrix with float values into a
                                              // viewable image form (float between values 0 and 1).

  //    magI.convertTo(magI, CV_8U, 1/255.0);
    //  cout<<magI.at<double>(1,1)<<endl<<magI.at<double>(1,1)<<endl<<magI.at<int>(1,1)<<endl;
      double sum=0,dt;
      int x,y;
      for (int i=10;i<310;i++)       //320
      for (int j=10;j<470;j++)       //480
        {
          x=abs(i-150);
          y=abs(j-230);
          sum+=(sqrt(x*x+y*y)/549)*magI.at<double>(i,j);
        }
    /*  cout<<sum<<endl;
      imshow("Input Image"       , I   );    // Show the result
      imshow("spectrum magnitude", magI);
      waitKey(0);
  */
      return sum;
  }