function [t,X,time,accuracy]= maxcut(path)
    %Read max cut instance
    fid=fopen(path);
    tline = fgets(fid);
    % Get the number of variables
    n=str2num(tline);
    W=zeros(n,n);
    % Read file and form the weight matrix
    while ischar(tline)
        tline = fgets(fid);
        if ischar(tline)
            C = textscan(tline,'%f %f %f');
            a=cell2mat(C);
            W(a(1)+1,a(2)+1)=a(3);
            W(a(2)+1,a(1)+1)=a(3);
        end

    end
    fclose(fid);
    %Form Laplacian Matrix L
    L=1/4*(diag(W*ones(n,1))-W);
    %Change max cut into minimize in order to be consistent for SeDumi
    c=-L(:);
    %Translate the constraint into matrix form: A*X=b
    A=sparse(1:n,1:n+1:n^2,ones(1,n),n,n^2);
    b=ones(n,1);
    %Define Semi positive definite constraint
    K.s=[n];
    %Run sedumi
    [X,Y,INFO]=sedumi(A,b,c,K);
    %Return results
    t=-c'*X;
    time=sum(INFO.timing);
    if INFO.numerr==0
       accuracy=10^(-8);
    elseif INFO.numerr==1
           accuracy =10^(-3);
    else
        accuracy=Inf;
    end
            
end
