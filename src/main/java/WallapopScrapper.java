import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class WallapopScrapper {
    public static void main(String[] args) throws InterruptedException {
        if (args.length != 1) {
            System.out.println("java -jar wallapop_scrapper.jar <url>");
            return;
        }

        WebDriver driver = new FirefoxDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get(args[0] + "/reviews");

        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button#onetrust-accept-btn-handler"))).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div.onetrust-pc-dark-filter")));
        TimeUnit.MILLISECONDS.sleep(250);
        cargarOpiniones(driver);

        // Sacar las URLs de los compradores y vendedores del usuario
        List<String> compradores = new ArrayList<>();
        List<String> vendedores = new ArrayList<>();

        for (WebElement opinion : driver.findElements(By.cssSelector("div.review-item"))) {
            List<WebElement> otroUsuarioPosible = opinion.findElements(By.cssSelector("a.review-microname"));
            if (otroUsuarioPosible.size() != 1) continue;

            String otroUsuario = otroUsuarioPosible.get(0).getAttribute("href") + "/reviews";

            switch (opinion.findElement(By.cssSelector(".review-type span")).getText()) {
                case "Vendió:" -> compradores.add(otroUsuario);
                case "Compró:" -> vendedores.add(otroUsuario);
            }
        }

        System.out.println("Compradores: " + compradores);
        System.out.println("Vendedores:  " + vendedores);

        // Sacar información de los productos comprados
        driver.switchTo().newWindow(WindowType.TAB);

        for (String vendedor : vendedores) {
            driver.navigate().to(vendedor);

            wait.until(ExpectedConditions.or(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.EmptyState")),
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.review-item"))));

            cargarOpiniones(driver);

            for (WebElement opinion : driver.findElements(By.cssSelector("div.review-item"))) {
                List<WebElement> otroUsuarioPosible = opinion.findElements(By.cssSelector("a.review-microname"));
                if (otroUsuarioPosible.size() != 1) continue;

                String otroUsuario = otroUsuarioPosible.get(0).getAttribute("href");

                if (otroUsuario.equals(args[0])) {
                    wait.until(ExpectedConditions.or(
                        ExpectedConditions.elementToBeClickable(By.cssSelector("a.review-title-item")),
                        ExpectedConditions.elementToBeClickable(By.cssSelector(".review-title span"))));

                    if (opinion.findElements(By.cssSelector(".review-title span")).size() > 0) continue;

                    opinion.findElement(By.cssSelector("a.review-title-item")).click();
                    driver.switchTo().window(new ArrayList<>(driver.getWindowHandles()).get(2));

                    Producto producto = null;

                    wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".main-header-logo img")));
                    if (driver.findElements(By.cssSelector("img.main-error-img")).size() == 0) {
                        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("h1")));

                        producto = new Producto(driver.getCurrentUrl(), args[0], otroUsuario);
                        detalles(driver, producto);
                    }

                    driver.close();
                    driver.switchTo().window(new ArrayList<>(driver.getWindowHandles()).get(1));

                    System.out.println(producto);
                }
            }
        }

        driver.quit();
    }

    static void cargarOpiniones(WebDriver driver) {
        while (driver.findElements(By.cssSelector("button.UserReviews__btn")).size() > 0) {
            try {
                driver.findElement(By.cssSelector("button.UserReviews__btn")).click();
            } catch (StaleElementReferenceException e) {
                //e.printStackTrace();
            }
        }
    }
    
    // "producto","comprador","vendedor","titulo,"precio","estado","descripcion","fecha"
    // ^Producto\{url='(.*)', comprador='(.*)', vendedor='(.*)', titulo='(.*)', precio='(.*)', estado='(.*)', descripcion='(.*)', fecha='(.*)'\}$
    // "$1","$2","$3","$4","$5","$6","$7","$8","$9"

    static void detalles(WebDriver driver, Producto producto) {
        List<WebElement> tituloPosible = driver.findElements(By.cssSelector("h1"));
        if (tituloPosible.size() == 1) producto.setTitulo(tituloPosible.get(0).getText());

        List<WebElement> precioPosible = driver.findElements(By.cssSelector("span.card-product-detail-price"));
        if (precioPosible.size() == 1) producto.setPrecio(precioPosible.get(0).getText());

        List<WebElement> estadoPosible = driver.findElements(By.cssSelector("span.ExtraInfo__text"));
        if (estadoPosible.size() == 1) producto.setEstado(estadoPosible.get(0).getText());

        List<WebElement> tipoPosible = driver.findElements(By.cssSelector("span.ExtraInfo__taxonomyBubble__text"));
        if (tipoPosible.size() == 1) producto.setTipo(tipoPosible.get(0).getText());

        List<WebElement> descripcionPosible = driver.findElements(By.cssSelector("p.js__card-product-detail--description"));
        if (descripcionPosible.size() == 1) producto.setDescripcion(descripcionPosible.get(0).getText());

        List<WebElement> fechaPosible = driver.findElements(By.cssSelector("div.card-product-detail-user-stats-published"));
        if (fechaPosible.size() == 1) producto.setFecha(fechaPosible.get(0).getText());
    }
}
