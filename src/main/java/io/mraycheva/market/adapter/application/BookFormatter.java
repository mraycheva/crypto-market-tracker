package io.mraycheva.market.adapter.application;

import io.mraycheva.market.order.Book;
import io.mraycheva.market.order.Order;
import io.mraycheva.market.order.OrderCollection;
import io.mraycheva.market.order.OrderSide;
import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.val;
import org.springframework.stereotype.Component;

@Component
class BookFormatter {

  String format(final Book book) {
    return String.format(
      """
                            
            <------------------------------------->
            %s
            %s
            %s
            %s
            %s
            %s
            >-------------------------------------<
                        
            """,
      formatAll(OrderSide.ASK.name().toLowerCase(), book.getAsks()),
      formatBest(OrderSide.BID.name().toLowerCase(), book.getBids()),
      formatBest(OrderSide.ASK.name().toLowerCase(), book.getAsks()),
      formatAll(OrderSide.BID.name().toLowerCase(), book.getBids()),
      book.getLastUpdate(),
      book.getCurrencyPair()
    );
  }

  private String formatAll(
    final String orderSide,
    final OrderCollection orderCollection
  ) {
    return String.format(
      """
            %ss:
            [ %s ]""",
      orderSide,
      format(orderCollection.getOrders())
    );
  }

  private String format(final Set<Order> orders) {
    return orders.stream().map(this::format).collect(Collectors.joining(",\n"));
  }

  private String formatBest(
    final String orderSide,
    final OrderCollection orderCollection
  ) {
    val bestOrder = orderCollection.getBest().map(this::format).getOrElse("[]");
    return String.format("best %s: %s", orderSide, bestOrder);
  }

  private String format(final Order order) {
    return String.format(
      "[%s, %s]",
      format(order.getPrice()),
      format(order.getVolume())
    );
  }

  private String format(final BigDecimal decimal) {
    return decimal.stripTrailingZeros().toPlainString();
  }
}
