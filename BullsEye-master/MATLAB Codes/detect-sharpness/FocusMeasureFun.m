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