package com.sashia.ecommerce.promotion.engine.pipeline;

import com.sashia.ecommerce.promotion.engine.context.PromotionContext;
import com.sashia.ecommerce.promotion.engine.pipeline.handler.PromotionHandler;
import com.sashia.ecommerce.promotion.engine.pipeline.handler.PromotionHandlerResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Executes the ordered promotion handlers for a single promotion.
 *
 * <p>Each handler may stop further processing by returning a failed
 * {@link PromotionHandlerResult}.
 */
@Component
public class PromotionPipeline {

    private static final Logger log = LoggerFactory.getLogger(PromotionPipeline.class);

    private final List<PromotionHandler> handlers;

    public PromotionPipeline(List<PromotionHandler> handlers) {
        this.handlers = handlers;
    }

    public void execute(PromotionContext context) {

        for (int i = 0; i < handlers.size(); i++) {

            var handler = handlers.get(i);

            log.debug("Executing handler {} by number ({},{})", handler.getClass().getSimpleName(), i + 1, handlers.size());

            if (!handler.handle(context).proceed())
                break;

        }
    }

}