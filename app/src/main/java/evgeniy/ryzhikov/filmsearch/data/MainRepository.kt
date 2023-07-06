package evgeniy.ryzhikov.filmsearch.data

import evgeniy.ryzhikov.filmsearch.R
import evgeniy.ryzhikov.filmsearch.domain.Film

class MainRepository {
    val filmsDataBase = listOf(
        Film(
            "Зеленая миля (1999)",
            R.drawable.film_1,
            "В тюрьме для смертников появляется заключенный с божественным даром. Мистическая драма по роману Стивена Кинга", 7.5f
        ),
        Film(
            "Список Шиндлера (1993)",
            R.drawable.film_2,
            "История немецкого промышленника, спасшего тысячи жизней во время Холокоста. Драма Стивена Спилберга", 6.5f
        ),
        Film(
            "Побег из Шоушенка (1994)",
            R.drawable.film_3,
            "Бухгалтер Энди Дюфрейн обвинён в убийстве собственной жены и её любовника. Оказавшись в тюрьме под названием Шоушенк, он сталкивается с жестокостью и беззаконием, царящими по обе стороны решётки. Каждый, кто попадает в эти стены, становится их рабом до конца жизни. Но Энди, обладающий живым умом и доброй душой, находит подход как к заключённым, так и к охранникам, добиваясь их особого к себе расположения.", 9.2f
        ),
        Film(
            "Форрест Гамп (1994)",
            R.drawable.film_4,
            "Полувековая история США глазами чудака из Алабамы. Абсолютная классика Роберта Земекиса с Томом Хэнксом", 4.5f
        ),
        Film(
            "Тайна Коко (2017)",
            R.drawable.film_5,
            "12-летний Мигель живёт в мексиканской деревушке в семье сапожников и тайно мечтает стать музыкантом. Тайно, потому что в его семье музыка считается проклятием. Когда-то его прапрадед оставил жену, прапрабабку Мигеля, ради мечты, которая теперь не даёт спокойно жить и его праправнуку. С тех пор музыкальная тема в семье стала табу. Мигель обнаруживает, что между ним и его любимым певцом Эрнесто де ла Крусом, ныне покойным, существует некая связь. Паренёк отправляется к своему кумиру в Страну Мёртвых, где встречает души предков. Мигель знакомится там с духом-скелетом по имени Гектор, который становится его проводником. Вдвоём они отправляются на поиски де ла Круса.", 2.3f
        ),
        Film(
            "Властелин колец: Возвращение короля (2003)",
            R.drawable.film_6,
            "Повелитель сил тьмы Саурон направляет свою бесчисленную армию под стены Минас-Тирита, крепости Последней Надежды. Он предвкушает близкую победу, но именно это мешает ему заметить две крохотные фигурки — хоббитов, приближающихся к Роковой Горе, где им предстоит уничтожить Кольцо Всевластья.", 6.2f
        ),
        Film(
            "Интерстеллар (2014)",
            R.drawable.film_7,
            "Когда засуха, пыльные бури и вымирание растений приводят человечество к продовольственному кризису, коллектив исследователей и учёных отправляется сквозь червоточину (которая предположительно соединяет области пространства-времени через большое расстояние) в путешествие, чтобы превзойти прежние ограничения для космических путешествий человека и найти планету с подходящими для человечества условиями.", 8.1f
        ),
        Film(
            "Криминальное чтиво (1994)",
            R.drawable.film_8,
            "Несколько связанных историй из жизни бандитов. Шедевр Квентина Тарантино, который изменил мировое кино", 4.5f
        ),
        Film(
            "Бойцовский клуб (1999)",
            R.drawable.film_9,
            "Страховой работник разрушает рутину своей благополучной жизни. Культовая драма по книге Чака Паланика", 1.2f
        ),
        Film(
            "Властелин колец: Братство Кольца (2001)",
            R.drawable.film_10,
            "Сказания о Средиземье — это хроника Великой войны за Кольцо, длившейся не одну тысячу лет. Тот, кто владел Кольцом, получал неограниченную власть, но был обязан служить злу.", 7.9f
        ),
    )
}