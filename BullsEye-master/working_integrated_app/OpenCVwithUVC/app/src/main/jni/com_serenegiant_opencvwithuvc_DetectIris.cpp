#include "com_serenegiant_opencvwithuvc_DetectIris.h"

JNIEXPORT void JNICALL Java_com_serenegiant_opencvwithuvc_DetectIris_DetectCenter
  (JNIEnv *env, jclass obj, jlong addrRgba,jlong addrGray) {
    Mat& frame=*(Mat *)addrRgba;
    Mat& frameg=*(Mat *)addrGray;
    centerx(frame,frameg);
  }
  int i=0,j=0;
  Mat element;
 void centerx(Mat &frame,Mat &frameg)
  {
    adaptiveThreshold(frameg,frameg,255, ADAPTIVE_THRESH_MEAN_C, THRESH_BINARY, 21, 2);
              element = getStructuringElement(MORPH_ELLIPSE, Size(11,11), Point(-1,-1) );
              morphologyEx(frameg,frameg,MORPH_OPEN,element,Point(-1,-1));
              morphologyEx(frameg,frameg,MORPH_CLOSE,element,Point(-1,-1));
              Canny(frameg,frameg,40,80,3,false);
              vector<Point>points;
              for(i=112;i<245;i++)
                for(j=70;j<190;j++)
                    if(frameg.at<int>(j,i)==255)
                        points.push_back(Point(i,j));
               Point center1(112,70);
                              Point center2(112,190);
                              Point center3(245,70);
                              Point center4(245,190);
              if(points.size()>5)
              {
                RotatedRect e = fitEllipse(points);

                Point center((int)e.center.x, (int)e.center.y);
              //ellipse(frame, e, Scalar(255,0,0),3,8);
                circle(frame, center, 2, Scalar(255,0,0), 10, 8, 0);
               circle(frame, center1, 2, Scalar(255,0,0), 10, 8, 0);
                              circle(frame, center2, 2, Scalar(255,0,0), 10, 8, 0);
                              circle(frame, center3, 2, Scalar(255,0,0), 10, 8, 0);
                              circle(frame, center4, 2, Scalar(255,0,0), 10, 8, 0);
               }



  }

