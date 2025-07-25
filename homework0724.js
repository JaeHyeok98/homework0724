selectBoardList(1);
const selectTag = document.querySelector('select[name=category]');
showSelectOptions(selectTag);

function showSelectOptions(selectTag) {
  const url = 'http://127.0.0.1:8080/api/board/category';
  return fetch(url)
    .then((resp) => resp.json())
    .then((categoryVoList) => {
      let str = ``;
      for (const vo of categoryVoList) {
        str += `
          <option value="${vo.categoryNo}">
            ${vo.categoryName}
          </option>
        `;
      }
      selectTag.innerHTML = str;
    });
}

function insert() {
  const title = document.querySelector('input[name=title]').value;
  const content = document.querySelector('textarea[name=content]').value;
  const categoryNo = document.querySelector('select[name=category]').value;
  const vo = {
    title,
    content,
    categoryNo,
  };

  const url = 'http://127.0.0.1:8080/api/board/insert';
  const option = {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(vo),
  };

  fetch(url, option)
    .then((resp) => resp.text())
    .then((data) => {
      if (data !== '1') {
        throw new Error();
      }
      Swal.fire('게시글 작성 성공!', '', 'success').then(() => {
        location.replace('http://192.168.20.208:5500/homework0724.html');
      });
    })
    .catch(() => {
      Swal.fire('게시글 작성 실패...', '', 'error');
    });
}

function selectBoardList(pno) {
  const url = `http://127.0.0.1:8080/api/board/${pno}`;
  const columnName = document.querySelector('select[name=searchSelect]').value;
  const searchInput = document.querySelector('input[name=searchInput]').value;
  const sortBy = document.querySelector('select[name=sortSelect]').value;
  const vo = {
    columnName,
    searchInput,
    sortBy,
  };
  const option = {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(vo),
  };
  fetch(url, option)
    .then((resp) => resp.json())
    .then((map) => {
      const voList = map.voList;
      const pvo = map.pvo;
      const tbody = document.querySelector('tbody');
      let str = '';
      for (const vo of voList) {
        str += `
              <tr onclick="selectOneByNo(${vo.no});">
                <td>${vo.no}</td>
                <td>${vo.title}</td>
                <td>${vo.content}</td>
                <td>${vo.categoryName}</td>
                <td>
                  <i 
                    class="fa-solid fa-trash-can" 
                    onclick="event.stopPropagation(); deleteBoard(${vo.no})" 
                    style="cursor: pointer;">
                  </i>
                </td>
              </tr>
            `;
      }
      tbody.innerHTML = str;
      // 페이지 버튼 생성
      const pageArea = document.querySelector('#page-area');
      let pageStr = '';
      if (pvo.startPage !== 1) {
        pageStr += `<button class="page-btn" onclick="selectBoardList(${
          pvo.startPage - 1
        })">이전</button>`;
      }
      for (let i = pvo.startPage; i <= pvo.endPage; i++) {
        pageStr += `<button class="page-btn${
          i === pvo.currentPage ? ' active' : ''
        }" onclick="selectBoardList(${i})">${i}</button>`;
      }
      if (pvo.endPage !== pvo.maxPage) {
        pageStr += `<button class="page-btn" onclick="selectBoardList(${
          pvo.endPage + 1
        })">다음</button>`;
      }
      pageArea.innerHTML = pageStr;
    })
    .catch(() => {
      Swal.fire('게시글 목록 조회 실패...', '', 'error');
    });
}

function selectOneByNo(no) {
  openModal();
  const url = 'http://127.0.0.1:8080/api/board/selectOne';
  const option = {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({ no }),
  };
  fetch(url, option)
    .then((resp) => resp.json())
    .then((vo) => {
      const detailDivTag = document.querySelector('.boardDetail');
      detailDivTag.innerHTML = `
          <h3>조회한 게시글</h3>
          <p><strong>번호 : </strong> ${vo.no}</p>
          <p>
            <strong>제목 : </strong>
            <input type="text" id="board-edit-title" value="${vo.title}" />
          </p>
          <p>
            <strong>내용 : </strong>
            <textarea id="board-edit-content">${vo.content}</textarea>
          </p>
          <p>
            <strong>카테고리 : </strong>
            <select class="detailCategory form-select" aria-label="Default select example"></select></p>
          <p><strong>작성일 : </strong> ${vo.createdAt}</p>
          <p><strong>수정일 : </strong> ${vo.modifiedAt}</p>
          <p><strong>조회수 : </strong> ${vo.hit}</p>
          <p><strong>삭제여부 : </strong> ${vo.delYn}</p>
          <br>
          <button onclick="editBoard(${vo.no})">수정하기</button>
        `;
      const detailSelectTag = document.querySelector('.detailCategory');
      boardDetailComment(vo.no);
      showSelectOptions(detailSelectTag).then(() => {
        detailSelectTag.value = vo.categoryNo;
      });
    });
}

function editBoard(no) {
  const title = document.querySelector('#board-edit-title').value;
  const content = document.querySelector('#board-edit-content').value;
  const categoryNo = document.querySelector('.detailCategory').value;
  const vo = {
    no,
    title,
    content,
    categoryNo,
  };
  const url = 'http://127.0.0.1:8080/api/board';
  const option = {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(vo),
  };
  fetch(url, option)
    .then((resp) => resp.text())
    .then((data) => {
      if (data !== '1') {
        throw new Error();
      }
      const pno = document.querySelector('.page-btn.active').textContent;
      Swal.fire('게시글 수정 성공!', '', 'success').then(() => {
        selectBoardList(pno);
        closeModal();
      });
    })
    .catch(() => {
      Swal.fire('게시글 작성 실패...', '', 'error');
    });
}

function deleteBoard(no) {
  const url = 'http://127.0.0.1:8080/api/board';
  const option = {
    method: 'DELETE',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({ no }),
  };
  Swal.fire({
    title: `정말 "삭제" 하시겠습니까?`,
    showDenyButton: true,
    showCancelButton: false,
    confirmButtonText: '예',
    denyButtonText: `아니오`,
  })
    .then((result) => {
      if (result.isConfirmed) {
        return true;
      } else if (result.isDenied) {
        return false;
      }
    })
    .then((isConfirmed) => {
      if (!isConfirmed) return;
      fetch(url, option)
        .then((resp) => resp.text())
        .then((data) => {
          if (data !== '1') {
            throw new Error();
          }
          Swal.fire('게시글 삭제 성공!', '', 'success').then(() => {
            location.replace('http://192.168.20.208:5500/homework0724.html');
          });
        })
        .catch(() => {
          Swal.fire('게시글 삭제 실패...', '', 'info');
        });
    });
}

function boardDetailComment(no) {
  const boardDetailCommentTag = document.querySelector('.boardDetailComment');
  const url = `http://127.0.0.1:8080/api/board/comment/${no}`;
  fetch(url)
    .then((resp) => resp.json())
    .then((voList) => {
      let str = `
      <h3>조회한 게시글 댓글</h3>
      <div class="comment-list">`;
      let i = voList.length;
      for (const vo of voList) {
        str += `
          <div class="comment-item">
            <p>[${i}번 댓글] ${vo.content}</p>
            <small>${vo.createdAt}</small>
            <i class="fa-solid fa-trash-can" onclick="deleteComment(${vo.boardCommentNo}, ${no})" style="cursor: pointer;"></i>
          </div>
        `;
        i--;
      }

      str += `</div>`;

      str += `
        <div class="comment-input">
          <textarea id="comment-content" placeholder="댓글을 입력하세요"></textarea>
          <br>
          <button onclick="insertBoardComment(${no})">댓글 등록</button>
        </div>
      `;

      boardDetailCommentTag.innerHTML = str;
    });
}

function insertBoardComment(boardNo) {
  const content = document.querySelector('#comment-content').value;
  const vo = {
    boardNo,
    content,
  };
  const url = `http://127.0.0.1:8080/api/board/comment/insert`;
  const option = {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(vo),
  };
  fetch(url, option)
    .then((resp) => resp.text())
    .then((data) => {
      if (data !== '1') {
        throw new Error();
      }
      Swal.fire('댓글 등록 성공!', '', 'success').then(() => {
        boardDetailComment(boardNo);
      });
    })
    .catch(() => {
      Swal.fire('댓글 등록 실패...', '', 'info');
    });
}

function deleteComment(boardCommentNo, boardNo) {
  const vo = {
    boardCommentNo,
    boardNo,
  };
  const url = `http://127.0.0.1:8080/api/board/comment`;
  const option = {
    method: 'DELETE',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(vo),
  };
  Swal.fire({
    title: `정말 "삭제" 하시겠습니까?`,
    showDenyButton: true,
    showCancelButton: false,
    confirmButtonText: '예',
    denyButtonText: `아니오`,
  })
    .then((result) => {
      if (result.isConfirmed) {
        return true;
      } else if (result.isDenied) {
        return false;
      }
    })
    .then((isConfirmed) => {
      if (!isConfirmed) return;
      fetch(url, option)
        .then((resp) => resp.text())
        .then((data) => {
          if (data !== '1') {
            throw new Error();
          }
          Swal.fire('댓글 삭제 성공!', '', 'success').then(() => {
            boardDetailComment(boardNo);
          });
        })
        .catch(() => {
          Swal.fire('댓글 삭제 실패...', '', 'info');
        });
    });
}

function openModal() {
  document.querySelector('.modal').classList.remove('hidden');
  document.querySelector('.modal-dim').classList.remove('hidden');
}

function closeModal() {
  document.querySelector('.modal').classList.add('hidden');
  document.querySelector('.modal-dim').classList.add('hidden');
}

document.querySelector('#sort-area').addEventListener('change', () => {
  selectBoardList(1);
});

// Swal.fire({
//   title: "Do you want to save the changes?",
//   showDenyButton: true,
//   showCancelButton: true,
//   confirmButtonText: "Save",
//   denyButtonText: `Don't save`
// }).then((result) => {
//   if (result.isConfirmed) {
//     Swal.fire("Saved!", "", "success");
//   } else if (result.isDenied) {
//     Swal.fire("Changes are not saved", "", "info");
//   }
// });
