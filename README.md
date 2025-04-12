Library — bu foydalanuvchilar kitoblarni  o'qish va qaytarish imkoniyatiga ega bo'lgan onlayn kutubxona tizimi. Tizim foydalanuvchi autentifikatsiyasini JWT (JSON Web Token) yordamida amalga oshiradi, kitoblarni qo'lga kiritish va qaytarish uchun API orqali muomala qilish mumkin. Tizimda foydalanuvchilar o'zlarining aktiv kitoblarini ko'rish, kitobni ijaraga olish va qaytarish imkoniyatiga ega bo'lishadi.
Qanday ishga tushiriladi:
Backendni ishga tushirish:

Java Spring Boot dasturini ishga tushirish uchun application.properties faylini sozlash va kerakli ma'lumotlar bazasini (masalan, PostgreSQL) ulash.

mvn spring-boot:run komandasini terminalda ishlatib backendni ishga tushiring.

Frontendni ishga tushirish:

HTML, CSS, va JavaScript yordamida frontendni yaratish va index.html yoki boshqa HTML faylini brauzerda ochish.

Frontend va backend o'rtasida Axios orqali aloqani ta'minlash.

JWT token olish:

Foydalanuvchi tizimga kirish uchun login sahifasidan foydalanib, JWT tokenini oladi.

Tokenni foydalanuvchi ma'lumotlarini autentifikatsiya qilish uchun tokenni headerda yuborish kerak.

Autentifikatsiya ishlashi haqida ma'lumot:
Autentifikatsiya jarayoni JWT orqali amalga oshiriladi. Foydalanuvchi tizimga kirish uchun quyidagi qadamlarni bajaradi:

Foydalanuvchi login sahifasiga kiradi va foydalanuvchi nomi hamda parolni kiritadi.

Tizim foydalanuvchi ma'lumotlarini tekshirib, agar to'g'ri bo'lsa, JWT tokenini yaratadi va uni foydalanuvchiga yuboradi.

Foydalanuvchi har bir so'rov yuborishda ushbu tokenni  headerida yuboradi.

Backend tokenni dekodlaydi va foydalanuvchini autentifikatsiya qiladi.

JWT token quyidagicha ishlaydi:

Header: Algoritm va token turi haqida ma'lumot.

Payload: Foydalanuvchi haqida ma'lumotlar (masalan, foydalanuvchi nomi yoki sub).

Signature: Tokenning haqiqiyligini tekshirish uchun ishlatiladi.


API namunalari (curl yoki Postman)

1. Foydalanuvchini registratsiya qilish qilish (Register)
POST  url:/api/auth/register
//////////////////////////////////////
//  {
    "username":"Olimjon",
    "password":"123456"

}
////////////////////////////////////
JAVOB:
///////////////////////////////////
User saved successfully
//////////////////////////

2 Foydalanuvchini autentifikatsiya qilish (Login)
POST url:/api/auth/login
//////////////////////////////////
{
    "username":"Olimjon",
    "password":"123456"

}
//////////////////////////////////
JAVOB
/////////////////////////////////
eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJPbGltam9uIiwicGFzc3dvcmQiOiIkMmEkMTAkSVMzU01BQS
55WVhyMnhDaWxDU3lRT2kuYktYLlJTYnJlaFkyWVNvelJsem9PdklQUWFDY2UiLCJyb2xlI
joiUk9MRV9VU0VSIiwiaWQiOjksInVzZXJuYW1lIjoiT2xpbWpvbiIsImlhdCI6MTc0NDQ3NTU4MCwiZXh
wIjoxNzQ0NDc5MTgwfQ.0K7oXw5nm0n7lhomPz3EcSn0RiRckynTxjzc-qfgNZ0
////////////////////////////////




