# Use the official Node.js image as a parent image
FROM node:latest

# Set the working directory
WORKDIR /app

# Copy package.json and package-lock.json (if present) to the working directory
COPY package.json package-lock.json ./

# Forzar la instalación de dependencias
RUN npm install --force

# Copy the rest of the application code
COPY . .

# Construir la aplicación
RUN npm run build

# Expose the port the app runs on
EXPOSE 3000

# Start the application
CMD ["npm", "start"]