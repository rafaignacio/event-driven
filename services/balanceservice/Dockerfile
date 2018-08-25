FROM microsoft/dotnet:2.1-sdk
RUN mkdir /app
WORKDIR /app

COPY . .
RUN dotnet publish -c Release -o out

EXPOSE 5000/tcp
CMD ["dotnet", "out/balanceservice.dll"]