# GitStar Android

Demo app for Step into Mobile // SiM 2018/11. This app uses GitHub's public API to list, search and star public repos.

## Screenshots

Later.

## Building instructions

Some aspects of this app require authorization, but implementing a fully-featured OAuth-based login flow was out of scope for this demo. To build the app, you need to obtain an OAuth token first:

1. Log in to github.com with your account.
2. Navigate to https://github.com/settings/tokens.
3. Generate a new Personal access token.
4. Use the contents of `keys.properties.sample` to create a config file called `keys.properties` and replace `INSERT_YOUR_TOKEN_HERE` with your personal access token (without quotes).
5. Open the project in Android Studio and proceed as usual.

**Important**: the generated APK contains your personal access token in plain text format. Don't share it with others.

## License

```
Copyright 2018 Tamás Szincsák

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
```
