---- name : GET_USERS_WITH_SALARY -----
select * from users
where salary > 1000;

---- name : GET_USERS_WITH_JOIN ------
select * from users u
inner join department d where u.deptid = d.id
where d.name = 'HR';

----- name : EMPLOYEE_QUERY ------
SELECT first_name, last_name, email, phone
FROM employees
WHERE department_id = ?
AND hire_date > ?
AND salary BETWEEN ? AND ?
ORDER BY last_name ASC
LIMIT ?;


----- name : COMPLEX_QUERY ------
WITH recent_sales AS (
    SELECT order_id, product_id, sale_date, quantity, total_amount
    FROM sales
    WHERE sale_date > ?
),
employee_performance AS (
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
