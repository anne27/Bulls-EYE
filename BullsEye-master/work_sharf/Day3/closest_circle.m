function circ = closest_circle(i,j,m,n,rad)
dist = euclid_dist(i,j,m,n);
%disp(dist)
act_dist = abs(rad-dist);
%disp(act_dist)
[~,circ] = min(act_dist);
%disp(act_dist)
end