package application.view;

import webserver.Model;
import webserver.handler.ViewHandler;
import webserver.http.HttpSession;
import webserver.http.request.HttpRequest;

public class MainPageHandler implements ViewHandler {

    private static final String MAIN_PAGE_RESOURCE_PATH = "/index.html";

    @Override
    public String handle(HttpRequest request, Model model) {
        HttpSession httpSession = request.getSession();

        //TODO: 현재 테스트를 위해 html 코드 전체 변경, 추후에 수정 예정
        String name = (String) httpSession.getAttribute("name");

        String html;
        if(name == null) {
            html =
                    """
                                      <li class="header__menu__item">
                                        <a class="btn btn_contained btn_size_s" href="/login">로그인</a>
                                      </li>
                                      <li class="header__menu__item">
                                        <a class="btn btn_ghost btn_size_s" href="/registration">
                                          회원 가입
                                        </a>
                                      </li>
                            
                            """;
        } else {
            html = String.format("""
                          <li class="header__menu__item">
                            <form method="POST" action="/logout">
                              <button class="btn btn_contained btn_size_s" type="submit">로그아웃</button>
                            </form>
                          </li>
                          <li class="header__menu__item">
                            <a class="btn btn_ghost btn_size_s" href="/mypage">%s</a>
                          </li>
                """, name);
        }

        model.setAttribute("header", html);

        return MAIN_PAGE_RESOURCE_PATH;
    }
}
