$(document).ready(function () {

    // var errorMessage = [[${errorMessage}]];
    //
    // if(errorMessage != null){
    //     alert(errorMessage);
    // }

    const navbar = document.getElementById('navbar');

    // 스크롤 이벤트 리스너 등록
    window.addEventListener('scroll', function() {

        // 스크롤 위치 확인
        if (window.scrollY > 745) { // 스크롤이 100px 이상 됐을 때
            navbar.classList.add('scrolled'); // 스크롤된 상태 클래스 추가
        } else {
            navbar.classList.remove('scrolled'); // 스크롤이 100px 미만이면 클래스 제거
        }
    });

    $(".SearchBtnByCityType").on("click",function (){
        searchByCityPage()
    });

    $(".SearchBtnByDestType").on("click",function (){
        searchByDest()
    });

    $(".searchTripCard").on("click",function (){
        searchTripCard()
    });

    $(".searchByMap").on("click",function (){
        searchByMap()
    });

    $("#searchBtn").on("click",function (){
        fastSearchByInput();
    });

    $("#taibeiCard").on("click",function (){
        location.href = "/exploreCity?searchArrival=臺北市";
    });

    $("#xinbeiCard").on("click",function (){
        location.href = "/exploreCity?searchArrival=新北市";
    });

    $("#taizhongCard").on("click",function (){
        location.href = "/exploreCity?searchArrival=臺中市";
    });

    $("#tainanCard").on("click",function (){
        location.href = "/exploreCity?searchArrival=臺南市";
    });

    $("#gaoxiongCard").on("click",function (){
        location.href = "/exploreCity?searchArrival=高雄市";
    });

    $("#hualianCard").on("click",function (){
        location.href = "/exploreCity?searchArrival=花蓮縣";
    });

    $("#taidongCard").on("click",function (){
        location.href = "/exploreCity?searchArrival=臺東縣";
    });

    $("#taoyuanCard").on("click",function (){
        location.href = "/exploreCity?searchArrival=桃園市";
    });



    $("#museumBtn").on("click",function (){
        var searchQuery = "博物館"
        var searchCity = document.getElementById('fastSearchCity').value;

        location.href = "/quickSearchList?searchQuery=" + searchQuery + "&searchCity=" + searchCity;
    });

    $("#artMuseumBtn").on("click",function (){
        var searchQuery = "美術館"
        var searchCity = document.getElementById('fastSearchCity').value;

        location.href = "/quickSearchList?searchQuery=" + searchQuery + "&searchCity=" + searchCity;

    });

    $("#parkBtn").on("click",function (){
        var searchQuery = "公園"
        var searchCity = document.getElementById('fastSearchCity').value;

        location.href = "/quickSearchList?searchQuery=" + searchQuery + "&searchCity=" + searchCity;

    });

    $("#marketBtn").on("click",function (){
        var searchQuery = "市場"
        var searchCity = document.getElementById('fastSearchCity').value;

        location.href = "/quickSearchList?searchQuery=" + searchQuery + "&searchCity=" + searchCity;

    });

    $("#templeBtn").on("click",function (){
        var searchQuery = "宮"
        var searchCity = document.getElementById('fastSearchCity').value;

        location.href = "/quickSearchList?searchQuery=" + searchQuery + "&searchCity=" + searchCity;

    });

    $("#nightMarketBtn").on("click",function (){
        var searchQuery = "夜市"
        var searchCity = document.getElementById('fastSearchCity').value;

        location.href = "/quickSearchList?searchQuery=" + searchQuery + "&searchCity=" + searchCity;

    });

    $("#playGroundBtn").on("click",function (){
        var searchQuery = "樂園"
        var searchCity = document.getElementById('fastSearchCity').value;

        location.href = "/quickSearchList?searchQuery=" + searchQuery + "&searchCity=" + searchCity;

    });

});

function searchTripCard() {

    var searchCity = document.getElementById('tripCardCity').value;

    location.href = "/exploreTripCard?tripCardCity=" + searchCity;
}

function searchByMap() {

    location.href = "/mapSearch";
}

// function searchByCityPage(page) {
//
//     var searchArrival = $("#cityTypeArriveCity").val();
//
//     location.href = "/explore/"+ page + "?searchArrival=" + searchArrival;
// }

function searchByCityPage() {

    var searchArrival = $("#cityTypeArriveCity").val();

    location.href = "/exploreCity" + "?searchArrival=" + searchArrival;
}

function fastSearchByInput() {

    //var geoOrigin = document.getElementById('origin').value;

    var searchQuery = document.getElementById('fastSearchInput').value;
    var searchCity = document.getElementById('fastSearchCity').value;

    location.href = "/quickSearchList?searchQuery=" + searchQuery + "&searchCity=" + searchCity;

}
