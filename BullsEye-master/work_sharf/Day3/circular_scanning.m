clear all && clc;
act_img = imread('test1.png');
act_img = act_img(:,220:805,:);
act_img = rgb2gray(act_img);
level = graythresh(act_img);
bin_img = im2bw(act_img,level);
[size_m,size_n] = size(bin_img);
%imshow(bin_img);
num_rings = 20;
ring_imgs = {};

for i=1:num_rings+1
    ring_imgs{i} = zeros(size_m,size_n);
end
num_segments = 1000;
step_size = 1;
for k=1:num_segments
    hit_rings = 1;
    white_fl=0;
    m = size_m/2;
    n = size_n/2;
    i = m;
    j = n;
    
    while i>1 && j>1 && i<size_m && j<size_n && hit_rings<num_rings
        i = i+(step_size)*sin(2*k*pi/num_segments);
        j = j+(step_size)*cos(2*k*pi/num_segments);
        
        if bin_img(max(round(i),1),max(round(j),1))==1
            if white_fl==0
                white_fl=1;
                hit_rings=hit_rings+1;
            end
            ring_imgs{hit_rings}(max(round(i),1),max(round(j),1))=1;
        end
        
        if bin_img(max(round(i),1),max(round(j),1))==0 && white_fl==1
            white_fl=0;
        end
    end
end