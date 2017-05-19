clc;
clear;
rgb=imread('eighteen.jpg');
BW = imclearborder(imcomplement(im2bw(imread('eighteen.jpg'))));
subplot(2,2,1); imshow(imread('eighteen.jpg'));
subplot(2,2,2); imshow(BW);
S= regionprops(BW, 'Perimeter','PixelIdxList');
[~,idx] = max([S.Perimeter]);
Indices = S(idx).PixelIdxList;
NewIm = false(size(BW));
NewIm(Indices) = 1;
subplot(2,2,3); imshow(NewIm);
%Create another matrix which removes all zero rows.
only_ones=NewIm(any(NewIm,2),:);        
%Might use this later.
[x1,y1,z1] = size(only_ones);
disp(x1);
disp(y1);

finish=0;
start=0;
diameter=0;                 %Initialise diameter.
for i=1:x1                  %Row iteration.
    flag=0;                 %Looking for start.
    for j=1:y1              %Column iteration.
        if only_ones(i,j)==1
            if flag==2
                continue;
            elseif flag==0
                start=j;
                flag=1;     %Found start.
            else
                finish=j;   %Found end.
                flag=2;
            end
        end
        distance=finish-start;
        if (distance>diameter)
            diameter=distance;
        end
    end
end        
disp(diameter);

F=1.1714e+03;
%Calculated using standard image im.jpg used in the code trial.m.
W=10.85;
%Measured by ruler.
P=diameter;
%Diamter in pixels, calculated above.
D=F*W/P;
disp(D);
