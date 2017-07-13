mypic=imread('im.jpg');
%im.jpg is the image with known distance (in this case 20cm).
I = rgb2gray(mypic);
BW = imclearborder(imcomplement(im2bw(imread('im.jpg'))));
%Clear image border.

subplot(2,2,1); imshow(imread('im.jpg'));
subplot(2,2,2); imshow(BW);
S= regionprops(BW, 'Perimeter','PixelIdxList');
[~,idx] = max([S.Perimeter]);
Indices = S(idx).PixelIdxList;
NewIm = false(size(BW));
NewIm(Indices) = 1;
subplot(2,2,3); imshow(NewIm);
%Create another matrix which removes all zero rows.
only_ones=NewIm(any(NewIm,2),:);        %Might use this later
[x1,y1,z1] = size(only_ones);
disp(x1);
disp(y1);
finish=0;
start=0;
diameter=0;                 %Initialise diameter
for i=1:x1                  %Row iteration
    flag=0;                 %Looking for start
    for j=1:y1              %Column iteration
        if only_ones(i,j)==1
            if flag==2
                continue;
            elseif flag==0
                start=j;
                flag=1;     %Found start
            else
                finish=j;   %Found end
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

%Now we have found the diameter (in pixels) of the outermost ring on paper.
%Compute focal length of camera by the formuala F=P*D/W where P=calculated
%diameter, D=Actual distance of object (in trial case, it was taken as
%20cm), W is the actual diamter in cm. (calculated to be 10.5 cm).

F=diameter*20/10.5;
disp('Displaying F=');
disp(F);

%Now process another image in a similar manner.
