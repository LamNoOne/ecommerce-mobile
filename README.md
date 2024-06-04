
# Ecommerce Mobile Application

#### Frontend: This application uses Jetpack Compose which applied MVVM Architecture, Google Auth OneTapCompose, Google Payment to accept the user's consent.

#### Backend: We use NodeJS(Express), MySQL, Sequelize ,PayPal Braintree to handle payment proccessing when Google Payment done.

## Architecture
#### Android Compose
![Architecture](https://res.cloudinary.com/dmnzkqysq/image/upload/v1717403482/Capture_khulpd.png)
#### Deployment Payment Function
![Payment](https://res.cloudinary.com/dmnzkqysq/image/upload/v1717489449/Untitled_gciukh.png)
#### Deployment Application
![Deployment](https://res.cloudinary.com/dmnzkqysq/image/upload/v1717489331/Architecture_imvs1e.jpg)


## API Reference

#### Sign up

```http
  POST /api/v2/auth/sign-up
```

| Header | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `x-api-version` | `number` | **Required**. API version |

| Body | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `lastName` | `string` | **Required**. Last name |
| `firstName` | `string` | **Required**. First name |
| `email` | `string` | **Required**. Email |
| `username` | `string` | **Required**. username |
| `phoneNumber` | `string` | **Required**. Phone number |
| `password` | `string` | **Required**. Password |

#### Sign in

```http
  POST /api/v2/auth/sign-in
```

| Header | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `x-api-version` | `number` | **Required**. API version |

| Body | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `username` | `string` | **Required**. Username |
| `password` | `string` | **Required**. Password |

#### Sign in with Google OAuth

```http
  POST /api/v2/auth/oauth?oauthTokenId=${oauthTokenId}
```

| Header | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `x-api-version` | `number` | **Required**. API version |

| Query Params | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `oauthTokenId` | `string` | **Required**. OAuth Token ID from Google |


#### Get information of current user

```http
  GET /api/v2/users/get-info
```

| Header | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `x-api-version` | `number` | **Required**. API version |
| `x-user-id` | `number` | **Required**. User ID |
| `Authorization` | `Bearer <token>` | **Required**. Access token |


#### Update profile of current user

```http
  PATCH /api/v2/users/update-profile
```

| Header | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `x-api-version` | `number` | **Required**. API version |
| `x-user-id` | `number` | **Required**. User ID |
| `Authorization` | `Bearer <token>` | **Required**. Access token |

| FormData | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `lastName` | `string` | **Required**. Last name |
| `firstName` | `string` | **Required**. First name |
| `phoneNumber` | `string` | **Required**. Phone number |
| `address` | `string` | **Required**. Address |
| `email` | `string` | **Required**. Email |


#### Get cart of current user

```http
  GET /api/v2/carts/get-my-cart
```

| Header | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `x-api-version` | `number` | **Required**. API version |
| `x-user-id` | `number` | **Required**. User ID |
| `Authorization` | `Bearer <token>` | **Required**. Access token |

#### Get selected products from cart

```http
  GET /api/v2/carts/get-selected-products
```

| Header | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `x-api-version` | `string` | **Required**. API version |
| `x-user-id` | `string` | **Required**. User ID |
| `Authorization` | `Bearer <token>` | **Required**. Access token |

| Body | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `productIds` | `array` | **Required**. Array of product IDs in cart |

#### Add product to cart

```http
  POST /api/v2/carts/add-to-cart
```

| Header | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `x-api-version` | `string` | **Required**. API version |
| `x-user-id` | `string` | **Required**. User ID |
| `Authorization` | `Bearer <token>` | **Required**. Access token |

| Body | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `productId` | `number` | **Required**. Product ID |
| `quantity` | `number` | **Required**. Quantity |

#### Update quantity of product in cart

```http
  POST /api/v2/carts/update-quantity-product
```

| Header | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `x-api-version` | `string` | **Required**. API version |
| `x-user-id` | `string` | **Required**. User ID |
| `Authorization` | `Bearer <token>` | **Required**. Access token |

| Body | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `productId` | `number` | **Required**. Product ID |
| `quantity` | `number` | **Required**. Quantity |


#### Delete product from cart

```http
  DELETE /api/v2/carts/delete-product-from-cart
```

| Header | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `x-api-version` | `string` | **Required**. API version |
| `x-user-id` | `string` | **Required**. User ID |
| `Authorization` | `Bearer <token>` | **Required**. Access token |

| Body | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `productId` | `number` | **Required**. Product ID |

#### Get categories

```http
  GET /api/v2/categories
```

| Header | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `x-api-version` | `string` | **Required**. API version |

#### Order from cart

```http
  GET /api/v2/checkout/order-from-cart
```

| Header | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `x-api-version` | `string` | **Required**. API version |
| `x-user-id` | `string` | **Required**. User ID |
| `Authorization` | `Bearer <token>` | **Required**. Access token |


| Body | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `shipAddress` | `string` | **Required**. Shipping address |
| `phoneNumber` | `string` | **Required**. Phone number |
| `paymentFormId` | `number` | **Required**. Payment form ID |
| `orderProducts` | `array` | **Required**. Order product IDs |

#### Get order

```http
  GET /api/v2/checkout/get-order?orderId=${orderId}
```

| Header | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `x-api-version` | `string` | **Required**. API version |
| `x-user-id` | `string` | **Required**. User ID |
| `Authorization` | `Bearer <token>` | **Required**. Access token |


| Query Params | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `orderId` | `number` | **Required**. Order ID |

#### Get all orders

```http
  GET /api/v2/checkout/get-all-orders?page=${page}&limit=${limit}&sortBy=${sortBy}&order=${order}&status=${status}&name=${name}
```

| Header | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `x-api-version` | `string` | **Required**. API version |
| `x-user-id` | `string` | **Required**. User ID |
| `Authorization` | `Bearer <token>` | **Required**. Access token |

| Query Params | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `page` | `integer` | **Optional**. Paging |
| `limit` | `integer` | **Optional**. Paging |
| `sortBy` | `string` | **Optional**. Order's sort field |
| `order` | `asc/desc` | **Optional**. Order's order type |
| `status` | `string` | **Optional**. Order's status |
| `name` | `string` | **Optional**. Order product's name |

#### Get products

```http
  GET /api/v2/products?name=${name}&categoryId=${categoryId}&page=${page}&limit=${limit}&sortBy=${sortBy}&order=${order}
```

| Header | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `x-api-version` | `string` | **Required**. API version |


| Query Params | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `name` | `string` | **Optional**. Product's name |
| `categoryId` | `integer` | **Optional**. Product's categoryId |
| `page` | `integer` | **Optional**. Paging |
| `limit` | `integer` | **Optional**. Paging |
| `sortBy` | `string` | **Optional**. Product's sort field |
| `order` | `asc/desc` | **Optional**. Product's order type |

#### Get product by ID

```http
  GET /api/v2/products/get-product?id=${id}
```

| Header | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `x-api-version` | `string` | **Required**. API version |


| Query Params | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `id` | `number` | **Required**. Product ID |

#### Get products by category

```http
  GET /api/v2/categories/get-products?id=${id}&page=${page}&limit=${limit}
```

| Header | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `x-api-version` | `string` | **Required**. API version |


| Query Params | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `id` | `number` | **Required**. Category ID |
| `page` | `integer` | **Optional**. Paging |
| `limit` | `integer` | **Optional**. Paging |

#### Get transaction

```http
  GET /api/v2/transaction?transactionId=${transactionId}
```

| Header | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `x-api-version` | `string` | **Required**. API version |
| `x-user-id` | `string` | **Required**. User ID |
| `Authorization` | `Bearer <token>` | **Required**. Access token |


| Query Params | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `transactionId` | `string` | **Required**. Transaction ID |

#### Make transaction

```http
  POST /api/v2/transaction
```

| Header | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `x-api-version` | `string` | **Required**. API version |
| `x-user-id` | `string` | **Required**. User ID |
| `Authorization` | `Bearer <token>` | **Required**. Access token |


| Body | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `orderId` | `number` | **Required**. Order ID |
| `nonce` | `number` | **Required**. Nonce ID |


