<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Mon Application</title>
    <link rel="stylesheet" href="path/to/your/styles.css"> <!-- Lien vers tes styles CSS -->
</head>
<body>

<jsp:include page="/layout/navbar.jsp" />

<section class="flex flex-col md:flex-row items-center justify-between p-8">
    <!-- Left Side (Text) -->
    <div class="w-full md:w-4/12 p-4">
        <h2 class="text-4xl md:text-6xl font-bold mb-2">Organisez enfin votre vie et votre travail.</h2>
        <p class="text-gray-700 text-2xl md:text-2xl">Simplifiez votre vie et celle de votre équipe avec l'application de task manager et de to do list la plus utilisée au monde.</p>
    </div>

    <!-- Right Side (Image) -->
    <div class="w-full md:w-8/12 p-4">
        <img src="https://th.bing.com/th/id/OIP.k0KFNQyfSr8h_0xSyljjuQHaDt?w=308&h=175&c=7&r=0&o=5&dpr=1.3&pid=1.7" alt="Description of the image" class="rounded-lg w-full h-auto">
    </div>
</section>





<jsp:include page="/layout/footer.jsp" />

</body>
</html>
