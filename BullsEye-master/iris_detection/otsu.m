mypic=imread('C:\Users\Anannya Uberoi\Desktop\eye\12.png');
mypic=rgb2gray(mypic);
h=imhist(mypic);

level = graythresh(mypic);
%Otsu's level for thresholding.
%Not using this currently but graythresh can be used later
%to obtain a better threshold value.

bwim1=adaptivethreshold(mypic,11,0.01,0);
cc=bwconncomp(bwim1);
numPixels = cellfun(@numel,cc.PixelIdxList);
[biggest,idx] = max(numPixels);
hold on;

[accum, circen, cirrad] = CircularHough_Grd(mypic, [15 105], 8, 10, 0.7);
figure(1); imagesc(accum); axis image;
figure(2); imagesc(mypic); colormap('gray'); axis image;
hold on;
plot(circen(:,1), circen(:,2), 'r+');
for k = 1 : size(circen, 1),
DrawCircle(circen(k,1), circen(k,2),cirrad(k), 32, 'b-');
end
hold off;
 
title('Iris');
 
% S=regionprops(cc,'Centroid');
% loc=S(idx).Centroid;
% plot(loc(:,1),loc(:,2),'b*');

%try
stats = regionprops('table',bwim1,'Centroid',...
'MajorAxisLength','MinorAxisLength');
centers=stats.Centroid;
diameters = mean([stats.MajorAxisLength stats.MinorAxisLength],2);
radii=diameters/2;
%viscircles(centers,radii);
%subplot(2,2,2),imshow(im2bw(mypic));
%subplot(2,2,2), imshow(mypic);
%try

function bw=adaptivethreshold(IM,ws,C,tm)
%ADAPTIVETHRESHOLD An adaptive thresholding algorithm that seperates the
%foreground from the background with nonuniform illumination.
%  bw=adaptivethreshold(IM,ws,C) outputs a binary image bw with the local 
%   threshold mean-C or median-C to the image IM.
%  ws is the local window size.
%  tm is 0 or 1, a switch between mean and median. tm=0 mean(default); tm=1 median.

IM=mat2gray(IM);
if tm==0
    mIM=imfilter(IM,fspecial('average',ws),'replicate');
else
    mIM=medfilt2(IM,[ws ws]);
end
sIM=mIM-IM-C;
bw=im2bw(sIM,0);
bw=imcomplement(bw);
end