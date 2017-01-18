Directory='F:\TUDelft\IN4301 Advanced Algorithms\Programming2\Instances0.5\';
ts=[];
Xs=[];
Times=[];
As=[];
FileNames=dir(Directory);
for i=3:size(FileNames,1)
    path=strcat(Directory,FileNames(i,1).name)
    [t,X,time,accuracy]=maxcut(path);
    ts=[ts t];
    Times=[Times time];
    As=[As accuracy];
end
x=[5 10 20 30 40 50 60 70]
plot(x,Times);