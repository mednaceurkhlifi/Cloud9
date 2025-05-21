# Smart Reservation System

This repository contains the full-stack implementation of the 4th year Cloud engineering project that lasted from Nov 2024 to May 2025 @Esprit.
**Cloud9** is the name of the team that worked on this project. **Queute**, is the web application and it's a **Smart Reservation System** with AI and blockchain integration. The system is designed to handle user reservations for services with optimal recommendations, roadmaps, news, forum and secure, verifiable transactions.

---

## 📁 Project Structure

```
.
├── README.md                # This file
├── frontend/                # Angular frontend
├── backend/                 # Spring Boot backend
├── Dockerfiles/             # Dockerfiles for frontend and backend
├── playbooks/               # Ansible playbooks for Kubernetes cluster setup and deployment
├── blockchain-test/         # Hardhat project for smart contract testing
├── presentations/           # Project-related presentations (PPT)
└── n8n/                     # n8n AI workflows
```

---

## 🧩 Technologies Used

- **IaaS**: [OpenStack](https://www.openstack.org/)  
- **Frontend**: Angular  
- **Backend**: Spring Boot (Java)  
- **Containerization**: Docker  
- **Automation**: Ansible
- **Orchestration**: Openstack heat, Kubernetes  
- **AI Automation**: [n8n](https://n8n.io/), Chatgpt, Gemini, HuggingFace..
- **Blockchain**: [Hardhat](https://hardhat.org/) for smart contract deployment & testing  

---

## 🚀 Deployment Instructions

1. **Cluster Setup**  
   Use the Ansible playbooks in the `playbooks/` directory to set up the Kubernetes cluster. Make sure to configure the Ansible inventory first.

2. **Docker Images**  

   To build the **frontend** image, navigate to the `frontend/` directory:
   ```bash
   docker build -f ../Dockerfiles/frontend -t queute-frontend .
   ```

   To build the **backend** image, navigate to the `backend/` directory:
   ```bash
   docker build -f ../Dockerfiles/backend -t queute-backend .
   ```

   You can also use the pre-built images on Docker Hub (tested on Azure):

   - [Frontend Image](https://hub.docker.com/repository/docker/naceur631/front-az/)
   - [Backend Image](https://hub.docker.com/repository/docker/naceur631/backend-az/)

3. **Run Containers**  
   Deploy the containers using Docker Compose or `docker run`.

4. **Blockchain Node**  
   Start the local Hardhat test network:
   ```bash
   cd blockchain-test
   npx hardhat node
   ```

5. **n8n Workflow**  
   Launch the n8n automation workflow engine:
   ```bash
   cd n8n
   docker-compose up -d
   ```

---

## 📊 Presentations

Project presentations are located in the `presentations/` directory.

---

