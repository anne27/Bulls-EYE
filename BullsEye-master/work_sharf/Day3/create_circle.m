function circ_imgs=create_circle(img,rad)
meridians = 1000;
step_size = 0.2;
[m,n] = size(img);
for k=1:meridians
    for co_ra=rad
        for r = co_ra
            i = m/2+r*sin(2*k*pi/meridians);
            j = n/2+r*cos(2*k*pi/meridians);
            img(round(i),round(j))=255;
        end
    end
end
done=1;
end