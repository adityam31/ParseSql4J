----- name : COMPLEX_QUERY_WITH_COMMENTS ------
WITH recent_sales AS (
    -- get recent sales
    SELECT order_id, product_id, sale_date, quantity, total_amount
    FROM sales
    WHERE sale_date > ?
),
employee_performance AS (
    --- get employee performance
    SELECT e.employee_id, e.first_name, e.last_name, SUM(s.total_amount) AS total_sales
    FROM employees e
    JOIN sales s ON e.employee_id = s.sales_rep_id
    WHERE s.sale_date BETWEEN ? AND ?
    GROUP BY e.employee_id, e.first_name, e.last_name
),
product_category_stats AS (
    SELECT p.product_id, p.category_id, COUNT(o.order_id) AS order_count, AVG(o.total_amount) AS avg_order_value
    FROM products p
    LEFT JOIN orders o ON p.product_id = o.product_id
    GROUP BY p.product_id, p.category_id
)
-- make joins to get final output
SELECT e.first_name, e.last_name, e.total_sales, p.product_name, c.category_name,
       ps.order_count, ps.avg_order_value, r.sale_date, r.quantity, r.total_amount
FROM recent_sales r
JOIN product_category_stats ps ON r.product_id = ps.product_id
JOIN product_categories c ON ps.category_id = c.category_id
JOIN products p ON r.product_id = p.product_id
JOIN employee_performance e ON p.sales_rep_id = e.employee_id
WHERE r.total_amount > ?
AND e.total_sales > ?
AND ps.order_count > ?
ORDER BY e.total_sales DESC, r.sale_date DESC;


----- name : COMPLEX_QUERY_WITH_COMMENTS_AND_BLANKS -----
-- Define a CTE to get the total sales and returns per product for a specific date range
WITH product_sales_returns AS (

    SELECT p.product_id,
           SUM(CASE WHEN t.transaction_type = 'SALE' THEN t.amount ELSE 0 END) AS total_sales,
           SUM(CASE WHEN t.transaction_type = 'RETURN' THEN t.amount ELSE 0 END) AS total_returns
    FROM products p
    LEFT JOIN transactions t ON p.product_id = t.product_id  -- Join products with transactions
    WHERE t.transaction_date BETWEEN ? AND ?  -- Filter transactions within a specific date range
    GROUP BY p.product_id

),
-- Define a CTE to calculate the average rating and the number of reviews per product
product_reviews AS (
    SELECT p.product_id,
           AVG(r.rating) AS avg_rating,
           COUNT(r.review_id) AS review_count
    FROM products p
    LEFT JOIN reviews r ON p.product_id = r.product_id  -- Join products with reviews
    GROUP BY p.product_id
),
-- Define a CTE to calculate sales performance of employees per region
employee_sales_performance AS (
    SELECT e.employee_id,
           e.region,
           SUM(t.amount) AS total_sales


    FROM employees e
    JOIN transactions t ON e.employee_id = t.sales_rep_id  -- Join employees with transactions
    WHERE t.transaction_date BETWEEN ? AND ?  -- Filter sales transactions within a date range
    GROUP BY e.employee_id, e.region
)
-- Final query: Combine all the CTEs and filter based on complex conditions
SELECT p.product_name,
       psr.total_sales,
       psr.total_returns,
       pr.avg_rating,
       pr.review_count,
       esp.region,
       esp.total_sales AS employee_sales,
       esp.employee_id
FROM products p
-- Join with the product sales and returns CTE


JOIN product_sales_returns psr ON p.product_id = psr.product_id


-- Join with the product reviews CTE
JOIN product_reviews pr ON p.product_id = pr.product_id


-- Join with the employee sales performance CTE
JOIN employee_sales_performance esp ON esp.employee_id = p.sales_rep_id
WHERE psr.total_sales > ?  -- Filter products with total sales above a certain amount

AND pr.avg_rating >= ?  -- Filter products with a minimum average rating
    AND esp.total_sales > ?  -- Filter employees with total sales above a threshold
ORDER BY pr.avg_rating DESC, psr.total_sales DESC;  -- Order by rating and sales
