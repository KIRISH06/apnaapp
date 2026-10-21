# OfflinePay - Complete Android Project & GitHub Build Setup

This repository contains the complete Android source code for **OfflinePay** (USSD *99# Automated Offline UPI Application).

## How to Upload to GitHub and Build APK Successfully:

### Option 1: Direct File Upload on GitHub Web (Easiest)
1. Download and extract **`OfflinePay_GitHub_Ready.zip`**.
2. Go to your GitHub repository: `https://github.com/KIRISH06/new-offline-`
3. Click **Add file** -> **Upload files**.
4. Drag and drop **ALL files and folders** inside the extracted `OfflinePay_GitHub_Ready` folder into GitHub:
   - `.github/`
   - `app/`
   - `gradle/`
   - `build.gradle.kts`
   - `settings.gradle.kts`
   - `gradle.properties`
   - `gradlew`
5. Click **Commit changes**.
6. Go to the **Actions** tab on GitHub — the APK build will start automatically!

### Option 2: Upload via Git Command Line
```bash
git init
git add .
git commit -m "Add complete OfflinePay project with gradlew wrapper"
git branch -M main
git remote add origin https://github.com/KIRISH06/new-offline-.git
git push -u origin main --force
```

When completed, go to **Actions** -> **Build OfflinePay APK** -> **Artifacts** to download `OfflinePay-APK.apk`.
