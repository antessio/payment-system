# Base image
FROM node:14-alpine

# Set working directory
WORKDIR /app

# Copy package.json and package-lock.json
COPY package*.json ./

# Install dependencies
RUN npm ci --production

# Copy source code
COPY . .

# Build the application (if necessary)
# RUN npm run build

# Expose a port (if needed)
EXPOSE 3000

# Command to start the application
CMD [ "npm", "start" ]
