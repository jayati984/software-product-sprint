// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

/**
 * Adds a random quote to the page.
 */
function addRandomQuote() {
  const quotes =
      ['I changed all my passwords to Incorrect. So whenever I forget, it will tell me Your password is Incorrect. - Michael Scott, The Office',
      'Welcome to the real world. It sucks. You’re gonna love it! — Monica Geller, Friends',
      'Bazinga! - Sheldon Cooper, The Big Bang Theory'];

  // Pick a random quote.
  const quote = quotes[Math.floor(Math.random() * quotes.length)];

  // Add it to the page.
  const quoteContainer = document.getElementById('quote-container');
  quoteContainer.innerText = quote;
}

/*function getComments() {
    fetch('/data').then(response => response.json()).then((comment) => {

    const arrayListElement = document.getElementById('comment-container');
    arrayListElement.innerHTML = '';
    arrayListElement.appendChild(
        createListElement('Comment 1: ' + comment.CommentOne));
    arrayListElement.appendChild(
        createListElement('Comment 2: ' + comment.CommentTwo));
    arrayListElement.appendChild(
        createListElement('Comment 3: ' + comment.CommentThree));
  });
}*/

/** Creates an <li> element containing text. */
/*function createListElement(text) {
  const liElement = document.createElement('li');
  liElement.innerText = text;
  return liElement;
}*/
