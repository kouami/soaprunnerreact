import React, {Component} from 'react';

class OutputMessageArea extends Component {
    render() {
        return(
            <div className="form-group">
                <label htmlFor="response">Response</label>
                <textarea className="form-control" id="request" rows="10" cols="20"></textarea>
            </div>
        );
    }
}

export default OutputMessageArea;