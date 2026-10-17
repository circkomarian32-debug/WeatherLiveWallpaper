# Збірка APK на Android через GitHub Actions

## 1. Створи репозиторій
На github.com створи новий порожній репозиторій `WeatherLiveWallpaper` (без README). Увійди у свій GitHub-акаунт.

## 2. Завантаж файли з Android через Termux
Встанови Termux із F-Droid. Завантаж цей ZIP у Downloads. У Termux введи:

```sh
pkg update && pkg install -y git unzip
termux-setup-storage
cd ~
mkdir -p weather-project && cd weather-project
unzip -o ~/storage/downloads/WeatherLiveWallpaper_GitHub_APK.zip
cd WeatherLiveWallpaper
git init -b main
git add .
git -c user.name='Android Builder' -c user.email='builder@example.invalid' commit -m 'Initial Android app and CI'
git remote add origin https://github.com/YOUR_USERNAME/WeatherLiveWallpaper.git
git push -u origin main
```

Заміни YOUR_USERNAME на свій GitHub-логін. GitHub для git push через HTTPS потребує персональний access token замість пароля (або налаштований SSH). Створи fine-grained token з Contents: Read and write для цього репозиторію. Не публікуй токен і нікому його не надсилай.

Якщо команда `git push` запитує Username — введи GitHub-логін; Password — токен. Якщо не працює `git init -b main`, використай `git init && git checkout -b main`.

## 3. Запусти збірку
У репозиторії GitHub відкрий Actions -> Build Android APK -> Run workflow (main). Також збірка запускається автоматично після push.

## 4. Завантаж APK
Після зеленої позначки відкрий успішний запуск Actions -> Artifacts -> WeatherLiveWallpaper-debug-APK. Завантаж ZIP артефакту, розпакуй, встанови `app-debug.apk`. Дозволь установлення APK із відповідного файлового менеджера, якщо Android запитає.

## 5. Налаштуй шпалери
Відкрий Weather Live Wallpaper -> Дозволити геолокацію -> Оновити погоду -> Встановити живі шпалери.

## Примітки
Це debug APK, придатний для особистого тестування, не підписаний release-ключем. Збірку ще не було запущено на GitHub, тому успіх компіляції не підтверджений. Проєкт використовує compileSdk 35, AGP 8.5.2, Gradle 8.7, Java 17. Для автоматичного визначення погоди потрібні дозволи геолокації та інтернет. Дані геолокації можуть бути відсутні до першого визначення місцеположення телефоном.
