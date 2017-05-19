%Algorithm obtained from research paper by Xie
%Wavelet Ratio Transform

clc;
clear;
filepath='C:\\Users\\Anannya Uberoi\\Desktop\\ball\\B';
%Edit the above variable to specify the file path.

max=0;
max_i=1;

for i=1:13
file_name=[filepath, num2str(i), '.png'];
mypic=imread(file_name);
%IMAGE=imread('C:\Users\Anannya Uberoi\Desktop\ball\B1.png');
FM=focal_measure_function(mypic);
if (FM>max)
    max=FM;
    max_i=i;
end
%Finding the maximum sharpness.
s=sprintf("B"+num2str(i)+":"+FM);
disp(s);
end

%Print out.
disp('The maximum sharpness is');
disp(max);
disp('The sharpest image is');
fprintf("B"+num2str(max_i)+"\n");

%ROI has not been extracted here, but can be
%done in case of inaccuracy later on.
function f_measure = focal_measure_function(Image, ROI)
if nargin>1 && ~isempty(ROI)
    Image = imcrop(Image, ROI);
end
        [C,S] = wavedec2(Image, 3, 'db6');
        H = abs(wrcoef2('h', C, S, 'db6', 1));   
        V = abs(wrcoef2('v', C, S, 'db6', 1));   
        D = abs(wrcoef2('d', C, S, 'db6', 1)); 
        A1 = abs(wrcoef2('a', C, S, 'db6', 1));
        A2 = abs(wrcoef2('a', C, S, 'db6', 2));
        A3 = abs(wrcoef2('a', C, S, 'db6', 3));
        A = A1 + A2 + A3;
        WH = H.^2 + V.^2 + D.^2;
        WH = mean2(WH);
        WL = mean2(A);
        f_measure = WH/WL;
    
 end
