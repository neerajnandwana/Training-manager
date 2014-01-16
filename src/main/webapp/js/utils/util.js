var Util = (function() {
	var prefix = 'uid-',
		uid = ['0', '0', '0'];
	return {
		nextUid : function() {
			var index = uid.length;
			var digit;

			while (index) {
				index--;
				digit = uid[index].charCodeAt(0);
				if (digit == 57 /* '9' */) {
					uid[index] = 'A';
					return uid.join('');
				}
				if (digit == 90 /* 'Z' */) {
					uid[index] = '0';
				} else {
					uid[index] = String.fromCharCode(digit + 1);
					return uid.join('');
				}
			}
			uid.unshift('0');
			return prefix + uid.join('');
		}
	};
})();