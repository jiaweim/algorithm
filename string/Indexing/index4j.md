# index4j

## 创建 Fm Index

```java
char[] text = "This is a long string\0".toCharArray();
char[] substring = "is".toCharArray();

FmIndex fmi = new FmIndex(text, 32, true);
```

3 个参数：

1. 待索引文本
2. 抽样率，例如 32 表示每 32 个 rows 保留一个 rank
3. 是否保存从 short 到 char 的映射，从而可以提取文本

获取基本统计信息：

`getAlphabetSize` 文本字符种类数：

- 如果原始字符包含 '\0'，该值与符号种类数相同
- 如果原始字符串不包含 '\0'，那么该值比符号种类数多 1

```java
char[] text = "This is a text".toCharArray();
FmIndex fmi = new FmIndex(text);
Set<Character> set = toSet(text);
assertEquals(set.size() + 1, fmi.getAlphabetSize());

text = "This is a text\0".toCharArray();
set = toSet(text);
fmi = new FmIndex(text);
assertEquals(set.size(), fmi.getAlphabetSize());
```

`getInputLength` 原始字符串长度+1：

```java
char[] text = "This is a text".toCharArray();
FmIndex fmi = new FmIndex(text);
assertEquals(text.length + 1, fmi.getInputLength());

text = "This is a text\0".toCharArray();
fmi = new FmIndex(text);
assertEquals(text.length + 1, fmi.getInputLength());
```

## 计数

 计算指定 pattern 出现的次数：`count(pattern)`

```java
char[] text = "This is a long string\0".toCharArray();
char[] substring = "is".toCharArray();

FmIndex fmi = new FmIndex(text, 32, false);
assertEquals(text.length + 1, fmi.getInputLength());

int matches = fmi.count(substring);
assertEquals(2, matches);
```

## 位置

查看每个 pattern 出现的位置：`locate`

```java
char[] text = "MKWVTFISSILLLLFSIAYSRGVFRRDTHKSIELK".toCharArray();
FmIndex fmi = new FmIndex(text);
IntArrayList sites = fmi.locate("SI".toCharArray(), 0, 2);
assertEquals(IntArrayList.of(8, 15, 30), sites);
```

## 提取字符串

在查询后提取字符串：到指定 `_` 停止

```java
char[] text = "MKWVTFISSILLLLFSIAYSRGVFRRDTHKSIELK_".toCharArray();
FmIndex fmi = new FmIndex(text);
IntArrayList sites = fmi.locate("SI".toCharArray(), 0, 2);
assertEquals(IntArrayList.of(8, 15, 30), sites);

int index = sites.getInt(0);
char[] target = new char[10];
int length = fmi.extractUntilBoundaryRight(index - 1, target, 0, 'F');
assertEquals("SILLLL", new String(target, 0, length));

index = sites.getInt(1);
length = fmi.extractUntilBoundaryRight(index - 1, target, 0, 'R');
assertEquals("SIAYS", new String(target, 0, length));

index = sites.getInt(2);
length = fmi.extractUntilBoundaryRight(index - 1, target, 0, '_');
assertEquals("SIELK", new String(target, 0, length));
```

两侧到指定字符串结束：

```java
```

