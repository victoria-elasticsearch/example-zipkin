FROM mcr.microsoft.com/dotnet/core/sdk:2.2 AS build-env
WORKDIR /app

# Copy csproj and restore as distinct layers
COPY *.csproj ./
RUN dotnet restore

# Copy everything else and build
COPY . ./
RUN dotnet publish -c Release -o out

# Build runtime image
FROM mcr.microsoft.com/dotnet/core/aspnet:2.2

ARG ZIPKIN_HOST
ARG ZIPKIN_PORT
ARG SERVICEGO_URL
ARG SERVICESPRING_URL

ENV ZIPKIN_HOST=$ZIPKIN_HOST
ENV ZIPKIN_PORT=$ZIPKIN_PORT
ENV SERVICEGO_URL=$SERVICEGO_URL
ENV SERVICESPRING_URL=$SERVICESPRING_URL

WORKDIR /app
COPY --from=build-env /app/out .
ENTRYPOINT ["dotnet", "ServiceA.dll"]