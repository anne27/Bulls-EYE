function circ_imgs = segment(bin_img)
    circ_range = 10:10:506;
    CC = bwlabel(bin_img);
    indcs = ones(size(unique(CC),1),1);
    [m,n] = size(bin_img);
    
    freq_list = {};


    for i=1:m
        for j=1:n
            if bin_img(i,j)==1
                idx = closest_circle(i,j,m/2,n/2,circ_range);
                label = CC(i,j);
                freq_list{label}(indcs(label))=idx;
                indcs(label)=indcs(label)+1;
                %disp(idx);
            end
        end
    end
    x = zeros(size(unique(CC),1));
    for i = 1:size(unique(CC),1)-1
        x(i) = mode(freq_list{i});
    end
    
    circ_imgs = {};
    for i=1:size(unique(x),1)+1
        circ_imgs{i} = zeros(size(bin_img));
    end
    
    for i=1:m
        for j=1:n
            if bin_img(i,j)==1
                label = CC(i,j);
                circ_imgs{x(label)}(i,j)=1;
            end
        end
    end
end