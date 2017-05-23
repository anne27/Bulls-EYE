act_img = imread('test1.png');
act_img = act_img(:,220:805,:);
img = rgb2gray(act_img);
I = img;
J = imresize(I,1.2);
[a,b] = sift(I);
[c,d] = sift(J);