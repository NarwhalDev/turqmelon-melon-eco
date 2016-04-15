# MelonEco
An economy plugin with support for multiple currencies.

## Why?
When working with clients, I found it easist to integrate MelonEco instead of trying to support Vault. MelonEco has native support for multiple currencies, and is highly customizable.

## Getting Started

1. Compile MelonEco against Java 8, making any changes you'd like
2. Place **MelonEco.jar** in your plugins directory
3. Run the server to generate your data file. (At the time of writing, MelonEco only supports YAML storage. SQL support is planned.)

As players join your server, they'll be given an Account. An account is a profile that has an associated nickname and list of balances for any currencies you have.

### Creating a Currency
Currencies are exactly what you'd think they are. If you want MelonEco to operate like any normal economy plugin, just make one.

To manage currencies, you'll need the **eco.currencies** permission.

#### /currency create (Singular) (Plural)
If I wanted to create a currency called **Coins**, I'd type **/currency create Coin Coins**.

When you make your first currency, it'll be the default. This means when referencing it for giving currency, or paying other players, you won't have to specify the name.

![Image](https://s3.amazonaws.com/f.cl.ly/items/0I0W2Y1A2N330F2f2t1P/Screen%20Shot%202016-04-14%20at%208.31.53%20PM.png?v=8d66281d)
_If we type /balance, we'll see our new currency._

Now, we want to tell MelonEco to start new players with 100 coins.

#### /currency startingbal Coins 100
When referencing your currency, you may use either its plural or singular name. It's interchangeable.

![Image](https://s3.amazonaws.com/f.cl.ly/items/163x091r0t3x002L2r19/Screen%20Shot%202016-04-14%20at%208.34.20%20PM.png?v=f4f10aae)

**Wait, why did your balance change when you already joined?**
The **Coins** currency for the **Turqmelon** account had never been defined. That means that I never spent any, or received any. This tells MelonEco that it's safe to update the balance.

If I spent 50, then gained another 50, and the starting balance was changed, my balance **would not** be affected.

### Decimal Currencies
Decimal currencies are fully supported by MelonEco. Currencies are always stored as decimals internally, however, the display of the currency is modified to make it more user-friendly and spending into decimals is prevented if you choose not to allow it.

When you create a currency, it will **not** be a decimal currency by default.

Let's make a new currency called Dollars.
#### /currency create Dollar Dollars

![Image](https://s3.amazonaws.com/f.cl.ly/items/0v452z0V0F2u270Z2C2F/Screen%20Shot%202016-04-14%20at%208.39.08%20PM.png?v=f53ae15d)
_The formatting of the balance command has been updated to look better with multiple currencies._

Now we'll enable decimal support.

#### /currency decimals Dollars

![Image](https://s3.amazonaws.com/f.cl.ly/items/0K2e0e0p0S0E0Q2i0V0x/Screen%20Shot%202016-04-14%20at%208.40.06%20PM.png?v=d956793f)

We can even add a prefix to the currency, to make it more friendly.

#### /currency symbol dollars $

![Image](https://s3.amazonaws.com/f.cl.ly/items/3p3M3m1O2O0P0b3I012h/Screen%20Shot%202016-04-14%20at%208.41.21%20PM.png?v=7a873149)

If we want to better differentiate the currencies, we can add colors to them

#### /currency color Coins GOLD
#### /currency color Dollars DARK_GREEN

![Image](https://s3.amazonaws.com/f.cl.ly/items/331q1s350Q3b391S2v2b/Screen%20Shot%202016-04-14%20at%208.42.58%20PM.png?v=f0c52b13)

If we want **Coins** to be a premium currnency, we can toggle players being able to pay them.

#### /currency payable coins

![Image](https://s3.amazonaws.com/f.cl.ly/items/0W0X3U0p0i0R0v152D3G/Screen%20Shot%202016-04-14%20at%208.43.55%20PM.png?v=3746f6b4)

Although, we may want to change the default currency now to dollars, so players don't have to type it when paying.

#### /currency default Dollars

## Giving and Taking Currency
The **Currency** argument for the following commands is optional. If a currency is not supplied, the default will be used.

Each command requires **eco.give**, **eco.take**, and **eco.set** respectively.

* **/ecogive (Account) (Amount) (Currency)** Gives (Currency) to the provided account
* **/ecoset (Account) (Amount) (Currency)** Sets the balance of (Currency) to the provided amount for the provided account
* **/ecotake (Account) (Amount) (Currency)** Takes (Currency) from the provided account

## Viewing Balances

Viewing balances requires the **eco.balance** permission, and can be done so via **/balance**, as well as popular aliases that are used by other economy plugins.
Viewing other account balances (**/balance (Account)**), requires **eco.balance.other**.

## Paying Currency
Paying currency requires **eco.pay**, and can be done with the command **/pay (Account) (Amount) (Currency)**. If no currency is supplied, the default will be used. Only currencies marked as **PAYABLE** (Default to TRUE) can be paid.

## Developer API
MelonEco was made to be developer-first.

All balances are stored in an ```Account```. You can retrieve accounts from the ```AccountManager```.

### Getting a Currency
```java

if (!AccountManager.getCurrencies().isEmpty()){ // Make sure the server has a currency
  Currency currency = AccountManager.getDefaultCurrency();
  
  System.out.println("Singular Name: " + currency.getSingular());
  System.out.println("Plural Name: " + currency.getPlural());
  
  // Using format() will automatically append the currency name, a symbol prefix (if specified), and make the provided double friendly
  System.out.println("Default Balance: " + currency.format(currency.getDefaultBalance()));
  
}

```

### Getting an Account
Accounts are created for a player the first time they join and don't have one. Accounts can be used by more then just players, though.

#### Getting an account by UUID
Each account has a UUID. If it's a player account, the account UUID will match the player UUID.

```java 
AccountManager.getAccount(player.getUniqueId());
```

#### Getting an account by nickname
Each account has a readable nickname. If it's a player account, it will match their most recent login name (and be updated if it doesn't match the next time they visit).

```java 
AccountManager.getAccount(player.getName());
```

#### Getting an account by online player
If the player is online, simply pass the player object.

```java 
AccountManager.getAccount(player);
```

#### Notes
It's important to always run these methods asyncronously. If the player provided is not online, then the DataStore will be accessed to find the account. This can lag the server if done often.
If ```getAccount()``` returns null, then there is no account that matches the provided query.

### Getting an Account Balance

```java
Account account = // my account
Player player = // my player
Currency currency = AccountManager.getDefaultCurrency();
double balance = account.getBalance(currency);
player.sendMessage("You have: " + currency.format(balance));
```

### Withdrawing Currency from an Account
```java
Account account = // my account
Currency currency = AccountManager.getDefaultCurrency();
double amount = 100;
if (account.withdraw(currency, amount)){
  // Withdraw was successful.
}
else{
  // Account has insufficient funds.
}

```

### Depositing Currency to an Account
```java
Account account = // my account
Currency currency = AccountManager.getDefaultCurrency();
double amount = 100;
if (account.deposit(currency, amount)){
  // Deposit was successful.
}
else{
  // Account doesn't allow deposits
}

```

### Creating an Account
Accounts can be created for other purposes then just player balances, as well (town or faction balances, auctions, etc.).

```java

Account account = new Account(UUID.randomUUID(), "My New Account");
MelonEco.getDataStore().saveAccount(account);

```

#### Notes
When modifying account or currency data, remember to save!

You may save by calling the following respectively:

```java
MelonEco.getDataStore().saveAccount(account);
MelonEco.getDataStore().saveCurrency(currency);
```
