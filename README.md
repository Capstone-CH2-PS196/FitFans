# FitFans
<b>Bangkit Academy Final Capstone Project</b>
<img src="https://github.com/Capstone-CH2-PS196/FitFans/blob/main/Assets/Logo.png" width="300" height="350">

## About
FitFans is an application that we created for the Bangkit Academy Capstone Project. FitFans is an education for gym beginners. This application is mobile-based and uses Machine Learning and Cloud Computing technology.

## Team Project
| Nama                                  | ID           | Path                 | Kampus                               | LinkedIn                             |
|---------------------------------------|--------------|----------------------|--------------------------------------|--------------------------------------|
| Fahri Rizki Saputra                   | M200BSY0577  | Machine Learning     | Universitas Diponegoro               | [LinkedIn](https://www.linkedin.com/in/fahri-rizki-saputra-417b86212/)                        |
| Oktavian Nikky Abprianto              | M254BSY0349  | Machine Learning     | Universitas Mercu Buana               | [LinkedIn](https://www.linkedin.com/in/oktavian-nikky-abprianto/)                        |
| Rega Anton Giapierro                   | M701BSY0612  | Machine Learning     | Universitas Primagraha                | [LinkedIn](https://www.linkedin.com/in/rega-anton-giapierro-297029226/)                        |
| Muhamad Faiz Riyadi                    | C701BSY3518  | Cloud Computing      | Universitas Primagraha                | [LinkedIn](https://www.linkedin.com/in/muhamad-faiz-riyadi-9aa111225/)                        |
| Saifuddaulah Alfarabi                  | C193BSY3026  | Cloud Computing      | Universitas Bina Sarana Informatika   | [LinkedIn](https://www.linkedin.com/in/saifuddaulah-alfarabi/)                        |
| Gerry Satria Halim                     | A128BSY2321  | Android Development  | Politeknik Negeri Jakarta             | [LinkedIn](https://www.linkedin.com/in/gerry-satria-halim-34722a142/)                        |
| Muchammad Raja Haikal Fiaugustian      | A248BSY2737  | Android Development  | Universitas Lampung                   | [LinkedIn](https://www.linkedin.com/in/muchammad-raja-haikal-f-0799a7280/)                        |

## How to install the API backend using Google Cloud Platform
<b> 1. Create a New Project on Google Cloud Platform</b><br>
<b>2. Create a Firewall rule </b><br>
<b>3. Create a Virtual Machine Compute Engine Instance</b><br>
<b>4. Create a Cloud SQL Instance</b><br>
<b>5. Create a Cloud Storage Bucket</b><br>
<i>note : make sure each other instances are connected by using firewall rule, target tags, and network ip 0.0.0.0/0 </i><br>
<b>6. Clone the Repository Github Branch Cloud-Computing</b><br>
```bash
git clone -b Cloud-Computing https://github.com/Capstone-CH2-PS196/FitFans.git
```
<b>7. Download Script Instalasi NVM</b><br>
```bash
curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.39.1/install.sh | bash
```
<b>8. Source the Bash Profile or Shell Profile in Use</b><br>
```bash
source ~/.bashrc
```
<b>9. Install Node.js Using NVM</b><br>
```bash
nvm install 18
```
<b>10. Install Python</b><br>
```bash
sudo apt update
sudo apt install build-essential zlib1g-dev libncurses5-dev libgdbm-dev libnss3-dev libssl-dev libreadline-dev libffi-dev libsqlite3-dev wget libbz2-dev
wget https://www.python.org/ftp/python/3.11.0/Python-3.11.0.tgz
tar -xf Python-3.11.0.tgz
cd Python-3.11.0
./configure --enable-optimizations
sudo make altinstall
```
<b>11. Enter the Cloud Computing Directory</b><br>
```bash
cd FitFans/Cloud-Computing
```
<b>12. Install Nodejs Dependency </b><br>
```bash
npm install
```
<b>13. Run Server NodeJs </b><br>
```bash
npm start
```
<b>14. Setup ENV python</b><br>
```bash
cd FitFans/Flask
```
<b>15. Install Virtual Environment and Dependency Python</b>
```bash
python3 -m venv tensorflow_env
source tensorflow_env/bin/activate
pip install tensorflow
pip install flask
pip install numpy
pip install pillow
```
<b>16. Run Server Python</b><br>
```bash
python app.py
```
