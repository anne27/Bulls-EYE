img = imread('eye3.jpg');
img = rgb2gray(img);
subplot(2,2,1);imshow(img);
num_seg = 1000;
rad = 1000*ones(1,251);
[m,n] = size(img);
temp_img = im2bw(img);
step_size = 0.2;
for k = 500:750
    i = m/2;
    j = n/2;
    while temp_img(round(i),round(j))==0 && i>(m/2-600) && j>(n/2-600) && i<(m/2+600) && j<(n/2+600)
        i = i+(step_size)*sin(k*pi/num_seg);
        j = j+(step_size)*cos(k*pi/num_seg);
    end
    rad(k-499)=euclid_dist(round(i),round(j),round(m/2),round(n/2));
end
rad = sort(rad);
avg_rad = mean(rad(:,100));
new_img = img;
for i=1:m
    for j=1:n
        if euclid_dist(i,j,round(m/2),round(n/2))>avg_rad
            new_img(i,j)=0;
        end
    end
end
subplot(2,2,2);imshow(new_img);

[~, threshold] = edge(new_img,0.0000001);
fudgeFactor = .1;

%BWs = canny(img, [], []);

BWs = edge(new_img,'sobel', threshold * fudgeFactor);
%subplot(2,2,3); imshow(BWs);

nBlack = sum(BWs(:));
nWhite = numel(BWs) - nBlack;
disp('No. of white pixels=');
disp(nWhite);

image_contrast = max(img(:)) - min(img(:));
disp(image_contrast);

%new_img=imadjust(new_img);

subplot(2,2,3); imshow(new_img);
 thresholdValue = 90;
 imgx = new_img > thresholdValue;
%imgx=im2bw(new_img, 0.2);

subplot(2,2,4); imshow(imgx);

function dist = euclid_dist(a,b,c,d)
dist = sqrt((a-c)^2+(b-d)^2);
end
