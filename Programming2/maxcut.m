function [upper,lower,n,X,delta_t,delta_ta,accuracy]= maxcut(path,itr)
    format long g
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
    %Cholesky factorization
    t0=clock;
    Xn=reshape(X,[n,n]);
    V=chol(Xn);
    max_lower=0;
    for i=1:itr
        %Generate a random vector
        R = normrnd(0,1,n,1);
        %Normalize the vector
        r= R/sqrt(sum(R.^2));
        %Rounding algorithm
        f=sign(V'*r)*sign(V'*r)';
        f=reshape(f,[n^2,1]);
        t_a=-c'*f;
        if t_a>max_lower
            max_lower=t_a;
        end
    end
    t1=clock;
    delta_ta=etime(t1,t0);
    %Return results
    upper=-c'*X;
    lower=max_lower;
    delta_t=sum(INFO.timing);
    if INFO.numerr==0
       accuracy=10^(-8);
    elseif INFO.numerr==1
           accuracy =10^(-3);
    else
        accuracy=Inf;
    end
            
end
