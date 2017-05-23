%Extract only The image
act_img = imread('test1.png');
act_img = act_img(:,220:805,:);
%imshow(act_img);
%Preprocessing starts here
img = rgb2gray(act_img);
level = graythresh(img);
bin_img = im2bw(img,level);
imshow(bin_img);
sel = strel('disk',1);
bin_img = imclose(bin_img,sel);
imshow(bin_img);

% circ_img = zeros(size(act_img));
% create_circle(circ_img);
circ_range = 10:10:506;
circ_imgs = segment(bin_img);
circ_range = 10:10:506;
% for i=1:28
%     figure,imshow(circ_imgs{i});
% end
%figure,imshow(temp_img);

for i=1:28
    figure,imshow(circ_imgs{i});
end