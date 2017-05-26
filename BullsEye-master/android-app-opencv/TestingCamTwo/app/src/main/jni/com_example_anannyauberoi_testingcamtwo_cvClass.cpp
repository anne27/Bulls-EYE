#include "com_example_anannyauberoi_testingcamtwo_cvClass.h"
#include <string>
#include <sstream>


JNIEXPORT jstring JNICALL Java_com_example_anannyauberoi_testingcamtwo_cvClass_detect
  (JNIEnv *env, jclass obj, jlong addrRgba)
  {
  Mat& frame=*(Mat*)addrRgba;
  double x=modifiedLaplacian(frame);
  std::ostringstream convert;
  convert << x;
  std::string Result=convert.str();
  const char *finalResult = Result.c_str();

  return env->NewStringUTF(finalResult);
  //detectfun(frame);
  }
/*
  void detectfun(Mat& frame)
  {
  String face_cascade_name = "/storage/emulated/0/xml/haarcascade_frontalface_alt.xml";
  String eyes_cascade_name = "/storage/emulated/0/xml/haarcascade_eye_tree_eyeglasses.xml";
  CascadeClassifier face_cascade;
  CascadeClassifier eyes_cascade;

  if( !face_cascade.load( face_cascade_name ) ){ printf("--(!)Error loading\n"); return; };
  if( !eyes_cascade.load( eyes_cascade_name ) ){ printf("--(!)Error loading\n"); return; };


  std::vector<Rect> faces;
  Mat frame_gray;

  cvtColor( frame, frame_gray, CV_BGR2GRAY );
  equalizeHist( frame_gray, frame_gray );

  //-- Detect faces
  face_cascade.detectMultiScale( frame_gray, faces, 1.1, 2, 0|CV_HAAR_SCALE_IMAGE, Size(30, 30) );

  for( size_t i = 0; i < faces.size(); i++ )
  {
    Point center( faces[i].x + faces[i].width*0.5, faces[i].y + faces[i].height*0.5 );
    ellipse( frame, center, Size( faces[i].width*0.5, faces[i].height*0.5), 0, 0, 360, Scalar( 255, 0, 255 ), 4, 8, 0 );

    Mat faceROI = frame_gray( faces[i] );
    std::vector<Rect> eyes;

    //-- In each face, detect eyes
    eyes_cascade.detectMultiScale( faceROI, eyes, 1.1, 2, 0 |CV_HAAR_SCALE_IMAGE, Size(30, 30) );

    for( size_t j = 0; j < eyes.size(); j++ )
    {
      Point center( faces[i].x + eyes[j].x + eyes[j].width*0.5, faces[i].y + eyes[j].y + eyes[j].height*0.5 );
      int radius = cvRound( (eyes[j].width + eyes[j].height)*0.25 );
      circle( frame, center, radius, Scalar( 255, 0, 0 ), 4, 8, 0 );
    }
  }
*/

/*double modifiedLaplacian(Mat& src)
{
Mat src_gray;
    cvtColor( src, src_gray, CV_BGR2GRAY );
    Mat M = (Mat_<double>(3, 1) << -1, 2, -1);
    Mat G = getGaussianKernel(3, -1, CV_64F);
    Mat Lx;
    sepFilter2D(src_gray, Lx, CV_64F, M, G);
    Mat Ly;
    sepFilter2D(src_gray, Ly, CV_64F, G, M);
    Mat FM = abs(Lx) + abs(Ly);
    double focusMeasure = mean(FM).val[0];
    return focusMeasure;
}*/


double modifiedLaplacian(Mat& src)
{
Mat src_gray;
    cvtColor( src, src_gray, CV_BGR2GRAY );
    Mat M = (Mat_<double>(3, 1) << -1, 2, -1);
    Mat G = getGaussianKernel(3, -1, CV_64F);
    Mat Lx;
    sepFilter2D(src_gray, Lx, CV_64F, M, G);
    Mat Ly;
    sepFilter2D(src_gray, Ly, CV_64F, G, M);
    Mat FM = abs(Lx) + abs(Ly);
    double focusMeasure = mean(FM).val[0];
    return focusMeasure;
    //return 2.2;
}