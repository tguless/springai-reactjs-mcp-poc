const path = require('path');
const { CleanWebpackPlugin } = require('clean-webpack-plugin');
const MiniCssExtractPlugin = require('mini-css-extract-plugin');

module.exports = {
  entry: './src/main/js/app.js',
  mode: 'development',
  cache: true,
  devtool: 'source-map',
  output: {
    path: path.resolve(__dirname, 'src/main/resources/static/built'),
    filename: 'bundle.js',
    publicPath: '/built/'
  },
  devServer: {
    historyApiFallback: true,
    port: 5000,
    host: 'localhost',
    static: [
      {
        directory: path.join(__dirname, 'src/main/resources/static'),
        publicPath: '/'
      },
      {
        directory: path.join(__dirname, 'src/main/resources/templates'),
        publicPath: '/'
      }
    ],
    devMiddleware: {
      publicPath: '/built/'
    },
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        secure: false,
        changeOrigin: true
      }
    },
    open: true
  },
  module: {
    rules: [
      {
        test: /\.js$/,
        exclude: /node_modules/,
        use: {
          loader: 'babel-loader',
          options: {
            presets: ['@babel/preset-env', '@babel/preset-react']
          }
        }
      },
      {
        test: /\.css$/,
        use: [
          MiniCssExtractPlugin.loader,
          'css-loader'
        ]
      },
      {
        test: /\.(png|svg|jpg|jpeg|gif)$/i,
        type: 'asset/resource',
        generator: {
          filename: 'images/[hash][ext][query]'
        }
      },
      {
        test: /\.(woff|woff2|eot|ttf|otf)$/i,
        type: 'asset/resource',
        generator: {
          filename: 'fonts/[hash][ext][query]'
        }
      }
    ]
  },
  plugins: [
    new CleanWebpackPlugin(),
    new MiniCssExtractPlugin({
      filename: 'styles.css'
    })
  ],
  resolve: {
    extensions: ['.js', '.jsx']
  }
}; 