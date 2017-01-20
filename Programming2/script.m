Directory='F:\TUDelft\IN4301 Advanced Algorithms\Programming2\Programming2\Instances1\';
uppers=[];
lowers=[];
ns=[];
Xs=[];
Delta_ts=[];
Delta_tas=[];
As=[];
FileNames=dir(Directory);
for i=3:size(FileNames,1)
    path=strcat(Directory,FileNames(i,1).name)
    [upper,lower,n,X,Delta_t,Delta_ta,accuracy]=maxcut(path,100);
    ns=[ns n]
    uppers=[uppers upper];
    lowers=[lowers,lower];
    Delta_ts=[Delta_ts Delta_t];
    Delta_tas=[Delta_tas Delta_ta];
    As=[As accuracy];
end
[sorted, indices] = sort(ns)
ns=ns(indices)
uppers=uppers(indices)
lowers=lowers(indices)
Delta_ts=Delta_ts(indices);
Delta_tas=Delta_tas(indices);
figure
subplot(1,2,1);
plot(ns,uppers,ns,lowers);
xlabel('Instance size');
ylabel('Solution');
legend('SDP','Rounding')
subplot(1,2,2);
plot(ns,Delta_ts,ns,Delta_tas);
xlabel('Instance size');
ylabel('Time(s)');
legend('SDP','Rounding')
figure
plot(ns,lowers./uppers)
xlabel('Instance size');
ylabel('Lower bound/Upper bound');